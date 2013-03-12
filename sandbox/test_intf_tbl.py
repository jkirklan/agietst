#!/usr/bin/python
d = ['up', 'eth4', '10.37.129.4']
c = ['down', 'eth5', '10.211.55.5']
e = ['up', 'eth1', '10.27.129.4']

title = ['status', 'intf_name', 'intf_ip']
foo = dict(zip(title,d))
foo1 = dict(zip(title,c))
foo2 = dict(zip(title,e))
intf_table = []
intf_name_tbl = []
search = 'eth4'
#def thebest():
#  entries = [d['key2'] for d in intf_table if d['key1']]
#  return len(entries), sum(entries)
intf_table.append(foo)
intf_table.append(foo1)
intf_table.append(foo2)
print intf_table
up = [ iface for iface in intf_table if iface.get('status') == 'up' ]
down = [ iface for iface in intf_table if iface.get('status') == 'down' ]
print down
print  up
count = len(up)
for count in up:
	intf_name_tbl.append(count['intf_name'])
print intf_name_tbl

