### Prerequisites

 To be able to run the back-end, you need to download and install the **Cassandra** server version 1.2. It can be downloaded **[here]**

### Keyspace creation

* Connect to the server using the **cassandra-cli** client
* Create the **achilles** keyspace with the following command: 
 `CREATE KEYSPACE achilles WITH placement_strategy = 'SimpleStrategy' AND strategy_options = {replication_factor:1};`
 
### Running the demo

* Checkout the source code of the demo with `git clone https://github.com/doanduyhai/Achilles-Twitter-Demo.git`
* Go to the demo folder and run it with `mvn jetty:run`

[here]: http://cassandra.apache.org/download/