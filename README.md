# AifadianPay
基于爱发电实现的spigot自动充值发货插件

使用:
前往 https://afdian.net 注册账号后，右上角点击"开发者"
获取user_id,token填入配置文件，重载插件，创建完商品即可开始使用(注意是商品，不是赞助方案)

配置文件:
```yaml
setting:
  # userId与token在爱发电获取,详情可参考mcbbs帖内教程
  userId: 'user_id'
  token: 'token'
  # 自动发货间隔
  delay: 3

shopItem:
  # 商品名称为爱发电内型号名称，请参考使用教程
  '100点券':
    # 展示名
    displayName: '100点券'
    # 使用 /apl show 100点券 时展示的url
    showUrl: 'https://afdian.net/item/1cab866647aa11ee9f3652540025c377'
    # 仅显示在展示信息 ( {price}RMB )
    price: 100
    # 获取点券
    point: 100
    # 执行指令
    commands:
      - 'give @p@ stone 1'
      - 'say @p@,感谢你的支持'
      - 'say 白给了啊，@p@刚刚赞助了100点券'
      - '[title]&a已发货/&f+@point@&a 点卷 (&c@price@rmb@&a)'
      - '[message]&a '
      - '[message]&f 已完成订单 &e@tradeNo@ &f感谢您的支持'
      - '[message]&f 当前点券余额 &e&n@current_point@'
      - '[message]&a '
```

命令&权限:
> /apl help 查看帮助 apl.use.help
> /apl check [订单] 查看订单状态 apl.admin.check
> /apl update [订单] 手动发货订单 apl.admin.update
> /apl reload 重载配置文件

其他:
自己包装的爱发电javasdk: https://github.com/meteorOSS/AifadianApi/tree/master
