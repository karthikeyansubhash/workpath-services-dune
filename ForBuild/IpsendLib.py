# -*- coding: utf-8 -*-
import sys, os, shutil, datetime, time
from time import gmtime, strftime
from optparse import OptionParser
import socket
import sys

Elapsed1st = datetime.datetime.now()
Elapsed2nd = datetime.datetime.now()

def printTimeTick(TickMessage):
    global Elapsed1st,Elapsed2nd
    timetick = datetime.datetime.now()
    print '***********',TickMessage, timetick - Elapsed1st, ",Total: ",timetick - Elapsed2nd
    Elapsed1st = timetick
    return timetick

def firePRN(ip, fileUrl):

    rtn = False

    if not os.path.exists(fileUrl):
        print 'file is not available', fileUrl
        return rtn
    else:
        #print 'file exist', fileUrl
        pass

    #printTimeTick('Opening files.. ')

    try:
        s = socket.socket()

        # print 'getdefaulttimeout =', socket.getdefaulttimeout()
        # socket.setdefaulttimeout(5)
        # print 'getdefaulttimeout =', socket.getdefaulttimeout()

        # print 'gettimeout =', s.gettimeout()
        s.settimeout(120)
        #print 'gettimeout =', s.gettimeout()

        s.connect((ip,9100))
        #s.create_connectionct((ip,9100), 5)
        f = open (fileUrl, "rb")
        l = f.read(1024)
        while (l):
            s.send(l)
            l = f.read(1024)

        rtn = True
    except Exception as e:
        print 'Exception with', e
        rtn = False
    finally:
        try:
            s.shutdown(socket.SHUT_WR)
            temp_rcv = s.recv(128)
            while temp_rcv:
                temp_rcv = s.recv(128)
        except Exception as e:
            print 'Exception with', e
        try:
            s.close()
        except Exception as e:
            print 'Exception with', e

    #printTimeTick('Sending files.. ')

    return rtn

def sendfile(ip, filename):
    port = 9100
    
    print "ip:%s, port:%d, filename:%s" % ( ip, int(port), filename)

    if not os.path.exists(filename):
        print 'file is not available', filename
        return False
    else:
        print 'file exist', filename

    printTimeTick('Opening files.. ')
    s = socket.socket()
    s.connect((ip,int(port)))
    f = open (filename, "rb") 
    l = f.read(1024)
    while (l):
        s.send(l)
        l = f.read(1024)
    s.close()    
    printTimeTick('Sending files.. ')
    return True

def executeBatch(ip, fileUrl):
    print ip, fileFolder
    print 'This methos will be supported later'
    return True

def executeIpPrnSend(ip, prn):
    print "Knock, knock, wake up " + ip
    return firePRN(ip, prn)

## test
# python IpsendLib.py -i 10.88.186.231 -f 22_BP_A3.prn
# python IpsendLib.py -i 10.88.186.231 -f text.txt

if __name__ == "__main__":
    """Main function to test this library"""

    #printTimeTick('Parsing arguments.. ')

    parser = OptionParser()

    parser.add_option("-i", "--ip", dest="ip",
                      help="specify ip address of machine that you want to send prn file.",
                      type='string',
                      default='')

    parser.add_option("-d", "--directory", dest="directory",
                      help="folder name of target",
                      type='string',
                      default='')

    parser.add_option("-f", "--filename", dest="filename",
                      help="file name of target",
                      type='string',
                      default='')

    parser.add_option("-t", "--timeout", dest="timeout",
                      help="timeout of testing.",
                      type='int',
                      default='10')

    parser.add_option("-c", "--contcount", dest="contcount",
                      help="continue count.",
                      type='int',
                      default=2)

    (options, args) = parser.parse_args()

    #printTimeTick('Starting functions.. ')
    result = firePRN(options.ip, options.filename)
    print "Sending File Result:%s" % (result)
