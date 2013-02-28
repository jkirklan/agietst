#!/usr/bin/python
import sys
from qpid.messaging import *

#global vars
broker_local = "localhost:5672"
addr_control = "agie_inbound/agie_inbound_control"

def broker_conn():
# create connection to local broker
        lb_connection = Connection(broker_local)
        try:
                lb_connection.open()
                session = lb_connection.session()
		receiver = session.receiver("agie_inbound_control")
		while True:
			message = receiver.fetch()
			print message.content
			session.acknowledge()
        except MessagingError,m:
                print m
        finally:
                lb_connection.close()

broker_conn()
