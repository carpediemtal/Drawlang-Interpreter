# Drawlang-Interpreter
详情请跳转[我的博客](https://linjinming.gitee.io/2020/11/24/%E5%87%BD%E6%95%B0%E7%BB%98%E5%9B%BE%E8%AF%AD%E8%A8%80%E4%B9%8B%E8%A7%A3%E9%87%8A%E5%99%A8/)

```
origin is (100, 300);
rot is 0;
scale is (1, 2);
for T from 0 to 100 step 1 draw (100 * cos(T), 100*sin(T));
scale is (2, 1);
for T from 0 to 2*PI step PI/50 draw (20 * cos(T), -20 * sin(T));
for T from 0 to 300 step 1 draw (T, -T);
for T from 0 to 500 step 5 draw (-T, -2 * T);

// 三个椭圆
origin is (380,340);
scale is (100,100/3);
rot is pi/2;
for T from -pi to pi step pi/50 draw (cos(t),sin(t));
rot is pi/2 + 2*pi/3;
for T from -pi to pi step pi/50 draw (cos(t),sin(t));
rot is pi/2 - 2*pi/3;
for T from -pi to pi step pi/50 draw (cos(t),sin(t));


// 新的图形:万花筒
origin is (250,250);
scale is (100,100);
rot is 0;
for t from 0 to 2*pi step pi/50 draw (cos(t), sin(t));
for t from 0 to pi*20 step Pi/50 draw ((1-1/(10/7))*cos(T)+1/(10/7)*cos(-T*(((10/7)-1))), (1-1/(10/7))*sin(T)+1/(10/7)*sin(-T*((10/7)-1)));
```
![Snipaste_2020-12-03_15-21-41.png](http://ww1.sinaimg.cn/large/005VT09Qly1glap1m307jj30i20euta2.jpg)

To be continued...
