> > > >####	btcpDbSupport 项目简介

>最新版本：0.0.1

>1、项目创建的目的是分离UD mapper 与 atomic 操作

>2、结构说明

>> a、mapper 所在位置：src/main/resources/mapper

>> b、代码所在位置  ：/src/main/java/com/lenovo/btcp/dbSupport/logs/atomic
   

>3、操作说明

>> a、在generatorConfig.xml 中配置需要生成的表名称及实体类名称

>> b、然后更新pom版本号 调用mvn deploy 命令把项目发布到 mvn仓库中

>> c、把对最新的代码提交到服务器上

>> d、对应系统调整为最新的客户端jar包就可以了

>> e、加入了 GenerateUdMapperTool 生成工具 生产的代码在 target\test-classes 文件夹下面 直接粘贴出来就可以了

					<dependency>
						<groupId>com.lenovo.btcp.btcpDbSupport</groupId>
						<artifactId>btcpDbSupport</artifactId>
						<version>0.0.1</version>
					</dependency>

