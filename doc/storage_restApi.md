# Hbase存储结构

## 表1
+ 表名： flume_machine_port_datatime_logid_metrics
- 行键rowkey："$MD5(agent-hostname)@$port;$T;$logId"
	+ $T 精确到秒
+ 列族cf：f
+ 列后缀名c：如 EventAcceptedCount ，ByteAcceptedSize

### 示例：
方案1（采用）

rowkey | cf | c(v) | c(v)
------|-----|-----|-----
8a352edd6e5e8f5ad196a875e1cfef93@8454;20151111 14:09:35;0000000007 | f | EventAcceptedCount(2000) | ByteAcceptedSize(5000)
8a352edd6e5e8f5ad196a875e1cfef93@8454;20151111 14:09:40;0000000007 | f | EventAcceptedCount(3000) | ByteAcceptedSize(6000)
5秒钟 表。

rowkey | cf | c(v) | c(v)
------|-----|-----|-----
8a352edd6e5e8f5ad196a875e1cfef93@8454;20151111 14:09;0000000007 | f | EventAcceptedCount(2000) | ByteAcceptedSize(5000)
8a352edd6e5e8f5ad196a875e1cfef93@8454;20151111 14:10;0000000007 | f | EventAcceptedCount(3000) | ByteAcceptedSize(6000)
1分钟 表。


----------
方案2（不采用）



rowkey | cf | c(v) | c(v)
------|-----|-----|-----
8a352edd6e5e8f5ad196a875e1cfef93@8454;20151111;0000000007_EventAcceptedCount | f | 14:09:35(2000) | 14:09:40(5000)
8a352edd6e5e8f5ad196a875e1cfef93@8454;20151111;0000000007_ByteAcceptedSize | f | 14:09:35(3000) | 14:09:40(6000)
5秒钟 表。这种方案是把时间粒度转化为了 hbase 的列，从而减少行 (row)。若采用这种存储方案，可以方便查看 某机器某天某日志的某指标。

两种方案都可以 扩展，计算每分钟、每5分钟、每小时 各指标的计数值，而且可以通过维度复制 自动叠加指标值。

ref url: http://developer.51cto.com/art/201510/493422.htm

## 表2
同时，为了满足业务查询需求，调转rowkey中 $logId 跟 $MD5(agent-hostname)@$port 的位置，进行冗余存储。

+ 表名： flume_logid_datatime_machine_port_metrics
- 行键rowkey："$logId;$T;$MD5(agent-hostname)@$port"
	+ $T 精确到秒
+ 列族cf：f
+ 列后缀名c：如 EventAcceptedCount ，ByteAcceptedSize





# 查询restful 接口

## 接口1

	http://$host:$port/query?_client_=machine_to_logids&machine=$machine&ts=$ts
	get post方式
	实现思路：实时查询hbase，rowkey前缀匹配，聚合并组织数据为json，渲染输出。
	
---------------------------------------

	返回结果示例：
	{
	    "$machine": {
	        "$logid1": {
	            "$time1": {
	                "EventAcceptedCount": 2000,
	                "ByteAcceptedSize": 5000
	            },
	            "$time2": {
	                "EventAcceptedCount": 2000,
	                "ByteAcceptedSize": 5000
	            }
	        },
	        "$logid2": {
	            "$time1": {
	                "EventAcceptedCount": 2000,
	                "ByteAcceptedSize": 5000
	            },
	            "$time2": {
	                "EventAcceptedCount": 2000,
	                "ByteAcceptedSize": 5000
	            }
	        }
	    }
	}
	
	{
	    "8a352edd6e5e8f5ad196a875e1cfef93@8454": {
	        "0000000008": {
	            "20151111 14:09:40": [
	                {
	                    "ByteAcceptedSize": "5500",
	                    "EventAcceptedCount": "2500"
	                }
	            ]
	        },
	        "0000000007": {
	            "20151111 14:09:35": [
	                {
	                    "ByteAcceptedSize": "5000",
	                    "EventAcceptedCount": "2000"
	                }
	            ],
	            "20151111 14:09:40": [
	                {
	                    "ByteAcceptedSize": "6000",
	                    "EventAcceptedCount": "3000"
	                }
	            ]
	        }
	    }
	}



## 接口2

	http://$host:$port/query?_client_=logid_to_machines&logid=$logid&ts=$ts
	get post方式
---------------------------------------

	返回结果示例：
	{
	    "$logid": [
	        {
	            "$machine1": [
	                {
	                    "$time1": {
	                        "EventAcceptedCount": 2000,
	                        "ByteAcceptedSize": 5000
	                    }
	                },
	                {
	                    "$time2": {
	                        "EventAcceptedCount": 2000,
	                        "ByteAcceptedSize": 5000
	                    }
	                }
	            ]
	        },
	        {
	            "$machine2": [
	                {
	                    "$time1": {
	                        "EventAcceptedCount": 2000,
	                        "ByteAcceptedSize": 5000
	                    }
	                },
	                {
	                    "$time2": {
	                        "EventAcceptedCount": 2000,
	                        "ByteAcceptedSize": 5000
	                    }
	                }
	            ]
	        }
	    ]
	}
	
	
前端定制单份机器为维度的json数据结构
host = {
	name: '',  // 机器名称
	cCountSum: 0,  // 当前总行数
	cSizeSum: 0,  // 当前总大小
	cLogSum: 0,  // 当前日志总份数
	logs: [log1,log2,...],  // 标志在该host下的以单份日志为维度的信息
	times: [time1, time2,...],  // 标志在该host下的以单个时间为维度的信息
};

log = {
	name: '',  // 日志名称
	countSum: 0,  // 日志总行数
	sizeSum: 0,  // 日志总大小
	cCount: 0,  // 日志当前行数
	cSize: 0,  // 日志当前大小
	startTime: '',  // 日志开始时间
	endTime: '',  // 日志结束时间
};

time = {
	time: '', // 当前时间
	logSum: '',  // 日志总份数
	countSum: 0,  // 日志总行数
	sizeSum: 0,  // 日志总大小
};
