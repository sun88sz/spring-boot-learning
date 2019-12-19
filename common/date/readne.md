整个地球分为二十四时区，每个时区都有自己的本地时间。在国际无线电通信场合，为了统一起见，使用一个统一的时间，称为通用协调时(UTC, Universal Time Coordinated)。UTC与格林尼治平均时(GMT, Greenwich Mean Time)一样，都与英国伦敦的本地时相同。在本文中，UTC与GMT含义完全相同。

北京时区是东八区，领先UTC八个小时，在电子邮件信头的Date域记为+0800。如果在电子邮件的信头中有这么一行：

Date: Fri, 08 Nov 2002 09:42:22 +0800

说明信件的发送地的地方时间是二○○二年十一月八号，星期五，早上九点四十二分（二十二秒），这个地方的本地时领先UTC八个小时(+0800， 就是东八区时间)。电子邮件信头的Date域使用二十四小时的时钟，而不使用AM和PM来标记上下午。

以这个电子邮件的发送时间为例，如果要把这个时间转化为UTC，可以使用一下公式：

UTC + 时区差 ＝ 本地时间



js

```
new Date().getTimezoneOffset()
-480

new Date(new Date().getTime()+new Date().getTimezoneOffset()*60000)
Thu Dec 19 2019 08:36:14 GMT+0800 (中国标准时间)

```