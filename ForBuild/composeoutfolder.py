#!/usr/bin/env python2

__author__ = 'sdk'
"""       compose-out-folder:
             composeoutfolder.py

Copyright (c) 2018 HP, Inc All rights reserved.
"""
import os
import os.path
import stat
import errno
import shutil
import string
import module_locator


def silentremove(filename):
    try:
        if os.path.isdir(filename):
            shutil.rmtree(filename, ignore_errors=True)
        else:
            os.remove(filename)
    except OSError as e: # this would be "except OSError, e:" before Python 2.6
        if e.errno != errno.ENOENT: # errno.ENOENT = no such file or directory
            raise # re-raise exception if a different error occurred


def move_rename(src, desc):
    """ Move sampleconfig for release"""
    try:
        if os.path.isdir(src):
            print "Failed to move " + src + " because of directory"
            return
        else:
            shutil.move(src, desc)
    except shutil.Error as e:
        print "Failed to move " + src + " because of " + e.message


def copy(my_path, out_folder, src, out_specific_folder, clear_build=False):
    src = my_path + src

    # Compose this file out path
    if not os.path.exists(src):
        print "Error! Artifact " + src + " wasn't prepared! \n"
        return

    out_name = out_folder + "/" + out_specific_folder + "/" + os.path.basename(src)

    try:
        os.makedirs(os.path.dirname(out_name))
    except OSError:
        pass

    try:
        if os.path.isdir(src):
            silentremove(out_name)
            shutil.copytree(src, out_name)
            if clear_build:
                silentremove(out_name + "/build")
        else:
            shutil.copy(src, os.path.dirname(out_name))
    except shutil.Error as e:
        print "Failed to copy " + src + " because of " + e.message


def copyRaw(my_path, out_folder, src, out_specific_folder, clear_build=False):
    src = my_path + src

    # Compose this file out path
    if not os.path.exists(src):
        print "Error! Artifact " + src + " wasn't prepared! \n"
        return

    out_name = out_folder + "/" + out_specific_folder

    try:
        os.makedirs(os.path.dirname(out_name))
    except OSError:
        pass

    try:
        if os.path.isdir(src):
            silentremove(out_name)
            shutil.copytree(src, out_name)
            if clear_build:
                silentremove(out_name + "/build")
        else:
            shutil.copy(src, os.path.dirname(out_name))
    except shutil.Error as e:
        print "Failed to copy " + src + " because of " + e.message


def makeWritable(path):
    for root, dirs, files in os.walk(path):
        for fname in files:
            full_path = os.path.join(root, fname)
            os.chmod(full_path, stat.S_IRUSR | stat.S_IWUSR | stat.S_IRGRP | stat.S_IWGRP | stat.S_IROTH | stat.S_IWOTH)  


my_path = module_locator.module_path() + "/"
android_home = os.environ['ANDROID_HOME']

classpath_string = [
    android_home + "extras/android/m2repository/com/android/support/support-annotations/24.1.0/support-annotations-24.1.0.jar:",
    my_path + "../Libs/JetAdvantageLinkLib/libs/gson-2.8.1.jar"]

def main():
    """ Main Function """
    if os.name == 'nt':
        my_path = module_locator.module_path() + "\\"
    else:
        my_path = module_locator.module_path() + "/"

    out_folder = my_path + "/../out/"

    # Clear all existing artifacts in workspace
    silentremove(out_folder)
    os.makedirs(out_folder)

    # Copy services apk and library aar
    # LinkForDevice
    #   - Docs
    #   - Libs
    #   - Samples
    # LinkForMobile
    #   - Docs
    #   - Libs
    #   - Samples
    # LinkForAll
    #   - Docs
    #   - Libs
    copy(my_path, out_folder, "../apps/workpathServices/build/outputs/apk/release/WorkpathServices-dune-release.apk", "LinkForDevice/Libs/target")
    move_rename(out_folder+"/LinkForDevice/Libs/target/WorkpathServices-dune-release.apk", out_folder+"/LinkForDevice/Libs/target/WorkpathServices-dune.apk")

    copy(my_path, out_folder, "../apps/workpathServices/build/outputs/apk/releaseForSim/WorkpathServices-dune-releaseForSim.apk", "LinkForDevice/Libs/simulator")
    move_rename(out_folder+"/LinkForDevice/Libs/simulator/WorkpathServices-dune-releaseForSim.apk", out_folder+"/LinkForDevice/Libs/simulator/WorkpathServices-dune.apk")

    copy(my_path, out_folder, "../apps/workpathServices/build/outputs/apk/release/output-metadata.json", "LinkForDevice/Libs")


if __name__ == "__main__":
    main()
