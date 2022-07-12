# Spring Kafka

## Kafka

kafka 는 LinkedIn 에서 처음 개발된 <code>분산 메시징 시스템</code> 이다. 

kafka 와 같은 Messaging-Queue 를 사용하기 전에는 End-to-End 연결방식의 아키텍쳐를 사용하였기 때문에
서비스가 커지면 커질수록 복잡성이 증가하고, 각기 다른 데이터 파이프라인 구조를 가지고 있었기 때문에 확장에 있어서 비용이 많이 들어갔다.

이러한 문제로 인해 모든 시스템으로 데이터를 전송할 수 있으면서 실시간 처리도 가능한 시스템이 Kafka 이다.

### Producer & Consumer

Producer 와 Consumer 말 그대로 Message 를 생성하고 소비하는 역할을 수행한다. Pub/Sub 에서 Publisher 와 Subscriber 의 역할.

Kafka 의 Consumer<sub>Subscriber</sub> 는 Broker 가 Consumer 에게 Message 를 전달하는 <code>Push</code> 방식이 아닌    
Consumer 가 Broker 에게서 Message 를 가져오는 <code>Pull</code> 방식을 채택하고있다.

이러한 <code>Pull</code> 방식의 장점은 Consumer 가 처리가능한 양 만큼 Message 를 가져오기 때문에 
Broker 는 Consumer 의 처리량을 신경쓰지 않아도 된다.

### Kafka Broker & Cluster

 - <code>Kafka Broker</code>는 실행된 Kafka Application Server 중 1대를 의미
 - 3대 이상의 Broker로 <code>Cluster</code>를 구축할 수 있다.

Cluster 는 <code>Zookeeper</code>와 연동을 해야한다. 이러한 Zookeeper의 역할은 Broker ID, Controller ID 등.. 메타데이터를 저장하는 역할을 한다.

N개의 Broker 중, 1대는 <code>Controller</code>의 역할을 수행한다. 여기서 Controller 는 각 Broker 에게 Partition 을 할당하거나, 
각 Broker의 상태가 정상인지 모니터링 하는 역할을 한다. Zookeeper 는 이러한 Controller 를 수행하는 Broker 는 누구인지 저장하고 있다.

### Record 

Producer 가 발행한 Message Data 를 Record 라고 한다.

### Topic & Partition 

<code>Topic</code>은 전달되는 Message Stream 의 추상화된 개념이다. (카프카의 데이터를 구분하기 위해 사용된다.)
Producer<sub>Publisher</sub> 가 특정 토픽에 Message 를 발행 시, Consumer<sub>Subscriber</sub> 가 해당 Topic 을 구독하여 소비할 수 있다.

<code>Partition</code>은 Topic을 분할한 단위이며, Topic은 하나 이상의 Partition 을 가지게 된다.

이러한 Partition 은 Kafka 에서 병렬처리를 하는데 핵심이 된다. Record 를 Partition 에 분산 배치 해줌으로써 Consumer 는 이러한 Record 를 병렬로 처리 할 수 있다.
(Data 가 Partition 에 분산 저장될 때는 Round-Robin 방식으로 저장된다.)

Partition 은 여러개의 <code>Log</code> 로 나뉘게 되는데 이러한 Log 에는 고유한 번호인 <code>Offset</code> 이 붙게된다. 배열의 Index 와 비슷한 개념이다.   
Offset 0번은 Queue 에 쌓인지 가장 오래된 Data 이며 가장 높은 수를 가진 Offset 은 가장 최근에 쌓인 Data 를 의미한다.

만약 Topic 이 하나의 Partition 을 가진다면 순서가 보장된다. Partition 은 Queue 구조이기 때문이다.
단, Topic 이 두개 이상의 Partition 을 가진다면 완전한 순서가 보장되지 않는다 두개의 Partition 이 Data 를 나누어 가지기 때문에 두개의 순서를 가지게 되는 셈이다.

 * Partition 설정 시 주의점은 Partition 은 한번 늘릴 시 절대로 줄일 수 없다. 따라서 운영중 Partition 을 늘릴때에는 신중하게 고민을 해보아야한다.

### Consumer Group

기존 Message Queue 에서는 Consumer<sub>Subscriber</sub> 가 Message 를 가져가면 Queue 에서 삭제되어 다른 Consumer 가 Message 를 소비할 수 없다.   

하지만 Kafka 는 Consumer 각 Message 를 소비해도 Queue 에서 Message 가 **즉시** 사라지지 않는다.   
따라서 하나의 Topic 에 여러 <code>Consumer Group</code> 가 붙어 메시지를 가져갈 수 있다.

이러한 Consumer Group 은 각 Group 마다 Partition 에 대한 별도의 Offset 을 관리한다. 
또한 Group 내 Consumer 가 Insert/Delete 될 때 마다 Re-balancing 을 하여 Group 내의 Consumer 에게 Partition 을 할당하게 된다.

한 Group 의 Consumer 는 Partition 과 1:1 또는 N:1 (Partition : Consumer) 관계를 맺게된다.   
Partition 3개, Consumer 2개로 이루어져 있다면 각 각의 Consumer 는 2:1, 1:1 관계를 맺는다.   
만약, Partition 2개, Consumer 3개로 이루어져 있다면 두 Consumer는 1:1 관계를 맺고 나머지 하나는 Idle 상태로 대기하게 된다.   
따라서, Partition과 Consumer는 1:1 을 권장하며 Consumer 의 성능에 따라 Partition >= Consumer 를 권장한다고 한다.

### Segment 

Kafka Broker 는 파티션에 저장된 메시지를 파일 시스템에 저장한다.이 때 만들어지는 파일이 <code>Segment File</code> 이다.   
기본적으로 일정 시간이 지나면 파일을 다시 만들지만 일정 시간 전 1GB 를 넘을 경우에도 새파일을 만든다.

* ref.1 : <https://www.youtube.com/watch?v=VJKZvOASvUA>
* ref.2 : <https://medium.com/@umanking/카프카에-대해서-이야기-하기전에-먼저-data에-대해서-이야기해보자-d2e3ca2f3c2>
* ref.3 : <https://jyeonth.tistory.com/30>
* ref.4 : <https://soft.plusblog.co.kr/3?category=896352>