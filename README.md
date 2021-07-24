# nitmirai-console-plugin-template

[Mirai Console](https://github.com/mamoe/mirai-console) 插件模板, 使用 Kotlin + Gradle.

[如何使用](https://github.com/project-mirai/how-to-use-plugin-template)

# 正文

## 数据类型

### 组群 Group

独立数据，与游戏关联

### 游戏 Game

独立数据，与实体关联

### 实体 Entity

独立数据，与游戏关联

实体内包含属性、名字等信息。

### 属性 Property

非独立数据

属性包含其值

## 指令

当前使用的指令集：

### game

- `/game|g create|use|open <name>` 创建/使用/打开 名为name的游戏，其中打开意为：若存在色使用，否则创建。
- `/game|g register_property|rp <name> [defaultValue] [defaultLimit]` 创建一个名为name，其默认值为defaultValue（默认为0），默认上限为defaultLimit（默认为-1，即无上限）。
- `/game|g unregister_property|up <name><#code>` 解除名为name或代码为code的属性。

### entity

- `/entity|e create <name>` 创建一个名为name的角色。
- `/entity|e delete <name>|<#uid> [confirm]` 销毁名为name或UID为#uid的角色，输入confirm表示确认。
- `/entity|e control <name>|<#uid>` 让当前玩家控制名为name或UID为#uid的角色。

### property

在进行属性操作的时候，如哦指定的属性未在对应的游戏中被注册的话，就会失败。

- `/property|prop|p <name> <set|add|minus|=|+|-> <value>` 将名为name的属性，设为/加/减 value代表的数值。
- `/property|prop|p <name> <remove|rm>` 将名为name的属性移除。

### check

- `/check|c <property_name> [normal|hard|extreme|n|h|e] [bonus|punish|b|p] [+|-|* <offset>]` 对名为property_name的属性，进行难度为 普通/困难/极难 的检定（默认为普通），允许使用 加/减/乘 对原始数值（未进行困难/极难减益的数值）进行修正。

## 示例

``` 
/game create "Under the Sight"

/game rp "int"
/game rp "str"
# ... register rest of the property types

/entity create "Edo"

/property "int" = 50
/property "str" = 75
# ... set rest properties
```

