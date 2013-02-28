#!/usr/bin/env python
import sys
import socket
import fcntl
import struct
import array
from time import sleep
import subprocess
import shlex

def pinger(iadr):
        command_line = "ping -c 1 " + iadr
        args = shlex.split(command_line)
        try:   
                subprocess.check_call(args,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
                print "network is available on ", intf[1]
        except:
                print "Couldn't get a ping on ", intf[1]
def pinger_b(iadr):
        command_line = "ping -c 1 " + iadr
        args = shlex.split(command_line)
        try:
                subprocess.check_call(args,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
                print "broker is there on ", iadr
        except:
                print "Couldn't get a ping on ", iadr

def all_interfaces():
    is_64bits = sys.maxsize > 2**32
    struct_size = 40 if is_64bits else 32
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    max_possible = 8 # initial value
    while True:
        bytes = max_possible * struct_size
        names = array.array('B', '\0' * bytes)
        outbytes = struct.unpack('iL', fcntl.ioctl(
            s.fileno(),
            0x8912,  # SIOCGIFCONF
            struct.pack('iL', bytes, names.buffer_info()[0])
        ))[0]
        if outbytes == bytes:
            max_possible *= 2
        else:
            break
    namestr = names.tostring()
    return [(namestr[i:i+16].split('\0', 1)[0],
             socket.inet_ntoa(namestr[i+20:i+24]))
            for i in range(0, outbytes, struct_size)]



start_config = all_interfaces()
del start_config[0]
for intf in start_config:
	broker = "broker." + intf[0] + ".example.com"
	intf_name = intf[0]
        intf_ip = intf[1]
        pinger(intf[1])
	pinger_b(broker)
	print "send message to avail brokers"
print start_config
while True:
	sleep(5)
	new_config = all_interfaces()
	del new_config[0]
	if start_config == new_config:
		print "no change"
		#start_config = new_config
	else:
		iffy = set(new_config)
		diffy = [x for x in start_config if x not in iffy]
		print "lost network", diffy
		for intf in new_config:
			intf_name = intf[0]
			intf_ip = intf[1]
			pinger(intf[1])
			broker = "broker." + intf[0] + ".example.com"
			pinger_b(broker)
		
		start_config = new_config
		print "new config", start_config
