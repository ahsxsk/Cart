# Cart
购物车
##目的
开发一个购物车，以HTTP调用的方式提供服务
##技术路线
- 框架:Spring+SpringMVC+Mybatis
- 总体想法:Mysql保证最终数据一致性，Redis做缓存提供高性能，Redis和Mysql之间使用MQ(Kafka)进行数据同步。

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
