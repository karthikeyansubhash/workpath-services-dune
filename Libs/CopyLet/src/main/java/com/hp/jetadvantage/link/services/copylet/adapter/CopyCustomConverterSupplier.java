package com.hp.jetadvantage.link.services.copylet.adapter;

import androidx.core.util.Pair;

import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.device.services.converter.DefaultTypeConverter;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;

import java.util.Map;
import java.util.function.Supplier;

public class CopyCustomConverterSupplier {
    public static class DuplexConverter implements Supplier<ITypeConverter> {
        @Override
        public ITypeConverter get() {
            return new DefaultTypeConverter<Pair<PlexMode, BindingFormat>, CopyAttributes.Duplex>(null,
                    CopyAttributes.Duplex.DEFAULT) {
                {
                    mapEtoW.putAll(Map.of(new Pair<>(PlexMode.PmSimplex, null), CopyAttributes.Duplex.NONE,
                            new Pair<>(PlexMode.PmDuplex, BindingFormat.BfFlipLeft), CopyAttributes.Duplex.BOOK,
                            new Pair<>(PlexMode.PmDuplex, BindingFormat.BfFlipUp), CopyAttributes.Duplex.FLIP));
                }

                @Override
                public CopyAttributes.Duplex convertEtoW(Pair<PlexMode, BindingFormat> pair) {
                    if (pair != null) {
                        Pair<PlexMode, BindingFormat> key = pair.first == PlexMode.PmSimplex ? new Pair<>(pair.first,
                                null) : pair;
                        return mapEtoW.getOrDefault(key, null);
                    }
                    return null;
                }

                @Override
                public CopyAttributes.Duplex convertEtoW(Pair<PlexMode, BindingFormat> pair,
                                                         boolean defaultIfNotFound) {
                    CopyAttributes.Duplex convertedValue = convertEtoW(pair);
                    return convertedValue != null ? convertedValue : (defaultIfNotFound ? getDefaultEnumValue() : null);
                }

                @Override
                public Pair<PlexMode, BindingFormat> convertWtoE(CopyAttributes.Duplex value) {
                    for (Map.Entry<Pair<PlexMode, BindingFormat>, CopyAttributes.Duplex> entry : mapEtoW.entrySet()) {
                        if (entry.getValue() == value) {
                            return entry.getKey();
                        }
                    }
                    return null;
                }

                @Override
                public Pair<PlexMode, BindingFormat> convertWtoE(CopyAttributes.Duplex value, Pair<PlexMode,
                        BindingFormat> defaultValue) {
                    Pair<PlexMode, BindingFormat> convertedValue = convertWtoE(value);
                    return convertedValue != null ? convertedValue : defaultValue;
                }

                @Override
                public Class<Pair<PlexMode, BindingFormat>> getE2Type() {
                    throw new UnsupportedOperationException("Duplex TypeConverter.getE2Type not supported.");
                }
            };
        }
    }
}
