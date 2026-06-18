/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import androidx.core.util.Pair;

import com.hp.ext.types.imaging.AutoCropModeType;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.device.services.converter.DefaultTypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;

import java.util.Map;
import java.util.function.Supplier;

public class ScanCustomConverterSupplier {
    public static class DuplexConverter implements Supplier<ITypeConverter> {
        @Override
        public ITypeConverter get() {
            return new DefaultTypeConverter<Pair<PlexMode, BindingFormat>, ScanAttributes.Duplex>(null,
                    ScanAttributes.Duplex.DEFAULT) {
                {
                    mapEtoW.putAll(Map.of(
                            new Pair<>(PlexMode.PmSimplex, null), ScanAttributes.Duplex.NONE,
                            new Pair<>(PlexMode.PmDuplex, BindingFormat.BfFlipLeft), ScanAttributes.Duplex.BOOK,
                            new Pair<>(PlexMode.PmDuplex, BindingFormat.BfFlipUp), ScanAttributes.Duplex.FLIP
                    ));
                }

                @Override
                public ScanAttributes.Duplex convertEtoW(Pair<PlexMode, BindingFormat> value) {
                    Pair<PlexMode, BindingFormat> key = value;
                    if (value != null) {
                        if (value.first == PlexMode.PmSimplex) {
                            key = new Pair<>(value.first, null);
                        }
                        return mapEtoW.get(key);
                    }
                    return null;
                }

                @Override
                public Class<Pair<PlexMode, BindingFormat>> getE2Type() {
                    throw new UnsupportedOperationException("duplex TypeConverter.getE2Type Not supported.");
                }
            };
        }
    }

    /**
     * Version-aware converter for AutoCropModeType ↔ ScanAttributes.CropMode.
     *
     * <p>EtoW behavior (version-dependent):
     * <ul>
     *   <li>AcmtOff         → CropMode.OFF  (all versions)</li>
     *   <li>AcmtContentCrop → CropMode.CROP_TO_CONTENT if clientVersion >= VERSION_LEVEL.SIX, else CropMode.ON</li>
     *   <li>AcmtPageCrop    → CropMode.CROP_TO_PAPER   if clientVersion >= VERSION_LEVEL.SIX, else CropMode.ON</li>
     * </ul>
     *
     * <p>WtoE behavior (version-independent, used when building scan ticket):
     * <ul>
     *   <li>CropMode.OFF             → AcmtOff</li>
     *   <li>CropMode.ON              → AcmtPageCrop</li>
     *   <li>CropMode.CROP_TO_PAPER   → AcmtPageCrop</li>
     *   <li>CropMode.CROP_TO_CONTENT → AcmtContentCrop</li>
     * </ul>
     *
     * <p>Usage with the current SDK version (default):
     * <pre>new ScanCustomConverterSupplier.CropModeConverter()</pre>
     *
     * <p>Usage with a specific client version (e.g., in scan job creation):
     * <pre>new ScanCustomConverterSupplier.CropModeConverter(clientVersion).get()</pre>
     */
    public static class CropModeConverter implements Supplier<ITypeConverter> {
        private final int clientVersion;

        /**
         * Uses the current SDK level as the default clientVersion.
         */
        public CropModeConverter() {
            this(Sdk.VERSION.LEVEL);
        }

        public CropModeConverter(int clientVersion) {
            this.clientVersion = clientVersion;
        }

        @Override
        public ITypeConverter get() {
            return new DefaultTypeConverter<AutoCropModeType, ScanAttributes.CropMode>(
                    AutoCropModeType.class,
                    ScanAttributes.CropMode.DEFAULT) {

                {
                    // WtoE reverse mapping (version-independent)
                    mapEtoW.put(AutoCropModeType.AcmtOff, ScanAttributes.CropMode.OFF);
                    mapEtoW.put(AutoCropModeType.AcmtPageCrop, ScanAttributes.CropMode.CROP_TO_PAPER);
                    mapEtoW.put(AutoCropModeType.AcmtContentCrop, ScanAttributes.CropMode.CROP_TO_CONTENT);
                }

                @Override
                public ScanAttributes.CropMode convertEtoW(AutoCropModeType value) {
                    if (value == null) return null;
                    if (value == AutoCropModeType.AcmtOff) {
                        return ScanAttributes.CropMode.OFF;
                    }
                    if (value == AutoCropModeType.AcmtContentCrop) {
                        return clientVersion >= Sdk.VERSION_LEVEL.SIX
                                ? ScanAttributes.CropMode.CROP_TO_CONTENT
                                : ScanAttributes.CropMode.ON;
                    }
                    if (value == AutoCropModeType.AcmtPageCrop) {
                        return clientVersion >= Sdk.VERSION_LEVEL.SIX
                                ? ScanAttributes.CropMode.CROP_TO_PAPER
                                : ScanAttributes.CropMode.ON;
                    }
                    return null;
                }
            };
        }
    }
}
