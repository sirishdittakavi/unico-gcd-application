

Assumptions:

Deployment Steps

1)Application server jboss-eap-6.3.
2)Start Jboss-eap-6.3 using standalone-gcd-queue.xml 
3)Create user under apprealm with 
     username as :appadmin password:Siri@1508
4)Deploy the gcd-application-ear.ear 


Sample links to access Rest and Soap services

1)http://localhost:8100/gcd/RestService/push?id1=6&id2=9

2)http://localhost:8100/gcd/RestService/list

3)http://localhost:8100/gcd/GCDSoapService?wsdl