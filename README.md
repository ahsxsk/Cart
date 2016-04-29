# Cart
  购物车
##目的
  开发一个购物车，以HTTP调用的方式提供服务
##技术路线
- 框架:Spring+Mybatis
- 总体想法:Mysql保证持久性持久性，最终一致性，Redis做缓存提供高性能，Redis和Mysql之间使用MQ(Kafka)进行数据同步。
- 开发注意事项:
>1 由于使用缓存+MQ进行“削峰填谷”，为防止MQ故障导致需要重放消息，下游必须做到幂等。

###MySql设计
####购物车主表设计
| 字段名     | 类型      |  备注  |
| -----      | -------   | ----   |
| id         | bigint    |自增主键|
| cartId     | varchar   |购物车条目ID，唯一键   |
| shopId     | varchar   |商品所属店铺   |
| skuId      | varchar   |商品唯一编号|
| amount     | tinyint   |商品数量|
| price      | int       |商品价格|
| userId     | varchar   |用户ID|
| status     | tinyint   |购物车状态，0正常， -1删除|
| crateTime  | timestamp  |加车时间|
| description| varchar   |保留使用|
| updateTime | timestamp  | 更新时间|
####Redis存储结构设计
　　购物车主要应用场景为查看购物车列表以及根据每条购物车详情下单等。购物车列表为一个用户对应多个不同的购物车信息，即多条购物车信息组合为一个用户的购物车。购物车详情为每条购物车信息对应多个属性，例如sku名称、价格、数量等。根据购物车使用场景及特点设计Redis存储方式为：
- 1 使用有序集合存储每个用户的所有购物车ID, 例如:
$$userId[cartId,…,cartId]$$
- 2 购物车详情使用散列存储每个购物车ID所对应的属性，例如:
$$cartId[(skuId, 123), …, (amount, 2)]$$
