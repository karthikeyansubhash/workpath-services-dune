#!/usr/bin/env python2
from ftputil.error import FTPOSError

__author__ = 's.starchenko'
"""       set-env-variables:

             setenvvariables.py

Read and sets necessary env variables to be used by the build.
Variables get stored in build.properties file.

April 21, 2015

Copyright (c) 2015 by Samsung Electronics, Inc All rights reserved.

This software is the confidential and proprietary information of Samsung
Electronics, Inc. ("Confidential Information"). You shall not disclose such
Confidential Information and shall use it only in accordance with the terms
of the license agreement you entered into with Samsung.
"""

import sys
import getopt
import os
import ftputil
import module_locator


properties_file_name = "build.properties"


def get_build_name(arg_model, arg_branch):
    """
    Gets onerom version (file name)
    :param arg_model: model to download onerom for
    :param arg_branch: branch to download onerom for
    :return: path of downloaded onerom
    """
    # Compose ftp path
    print "Connecting to FTP for firmware..."
    ftp_ip = "10.88.194.92"
    ftp_path = "SubFW/" + arg_model + "/TGIF/ONEROM/tgif/"

    onerom_short_name = None

    try:
        with ftputil.FTPHost(ftp_ip, "everest", "autobuild") as ftp_host:
            ftp_host.chdir(ftp_path)
            files = ftp_host.listdir(ftp_host.curdir)

            for name in files:
                if ftp_host.path.isfile(name) and name.endswith("hds"):
                    print "Found onerom file", name
                    onerom_short_name = name[0:-4]

                    print "\nConfiguration:"
                    print "[BUILD]", name[:-4]
                    break
    except FTPOSError:
        onerom_short_name = "N/A"

    return onerom_short_name


def store_properties_file(my_path, arg_ip, arg_model, arg_branch, onerom_short_name):
    """
    Saves properties to file to create env variables from

    :param my_path: path to
    :param arg_ip: of the device
    :param arg_model: model
    :param arg_branch: branch
    :param onerom_short_name: short name of onerom file
    :return:
    """
    properties_full_path = os.path.join(my_path, properties_file_name)

    if os.path.isfile(properties_full_path):
        os.remove(properties_full_path)

    with open(properties_full_path, 'w') as outfile:
        outfile.write("# DUT Configuration\n")
        outfile.write("SMARTUX_DUT_IP=" + arg_ip + "\n")
        outfile.write("SMARTUX_DUT_MODEL=" + arg_model + "\n")
        outfile.write("SMARTUX_DUT_MODEL_BRANCH=" + arg_branch + "\n")
        outfile.write("# Build ID\n")
        outfile.write("SMARTUX_BUILD_ID=" + onerom_short_name + "\n")
        outfile.flush()
        outfile.close()


def main():
    """ Great Main Function """
    # Read input params
    try:
        opts, args = getopt.getopt(sys.argv[1:], "i:m:b:s",
                                   ["ip=", "model=", "branch=", "skip"])
    except getopt.GetoptError:
        print "Wrong parameters"
        sys.exit(2)

    if os.name == 'nt':
        my_path = module_locator.module_path() + "\\"
    else:
        my_path = module_locator.module_path() + "/"

    arg_ip = None
    arg_model = None
    arg_branch = None
    arg_skip = False

    for opt, arg in opts:
        if opt in ("-i", "--ip"):
            arg_ip = arg
        elif opt in ("-m", "--model"):
            arg_model = arg
        elif opt in ("-b", "--branch"):
            arg_branch = arg
        elif opt in ("-s", "--skip"):
            arg_skip = True
        elif opt in ("-h", "--help"):
            sys.exit(0)

    if arg_ip is None:
        print "Error! --ip argument should be presented!\n"
        sys.exit(1)

    if arg_model is None:
        print "Error! --model argument should be presented!\n"
        sys.exit(1)

    if arg_branch is None:
        print "Error! --branch argument should be presented!\n"
        sys.exit(1)

    onerom_short_name = "N/A"
    if not arg_skip:
        onerom_short_name = get_build_name(arg_model, arg_branch)
    else:
        print "Skipping FTP check...\n"

    # Store properties file for further usage
    store_properties_file(my_path, arg_ip, arg_model, arg_branch, onerom_short_name)


if __name__ == "__main__":
    main()