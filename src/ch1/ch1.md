# Java8流库

先举个例子:
```
    long count = words.stream().filter(w -> w.length() > 12).count();
```

结合这个例子解释流的一些特性:
1. 流不会存储元素,元素可能存储在底层的集合中,或者是按需生成的
2. 流的操作不会修改数据源,例如上面的filter操作不会移除words中的元素,而是新建一个流,其中不包括被过滤掉的元素
3. 流的操作是尽可能惰性执行的.也就是直到需要结果的时候才会执行操作.比如我们只要5个单词,那么流就会在获得5个单词之后就停止过滤.

API:
```
    Stream<T> filter(Predicate<? super T> p);
```
## 流的创建:
Collection接口的方法:
```
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
```
Stream中的静态方法:
```
    public static<T> Stream<T> of(T... values) {
        return Arrays.stream(values);
    }
    
    public static<T> Stream<T> of(T t) {
        return StreamSupport.stream(new Streams.StreamBuilderImpl<>(t), false);
    }
    
    public static<T> Stream<T> empty();

```
Array中的静态方法:可以从数组中位于from(包括)到to(不包括)的元素创建一个流
```
    Array.stream(array, from, to);
```
生成无限流:
```
    Stream.generate();
    Stream<Double> randoms = Stream.generate(Math::random);
    
    Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
    
```


## filter,map,flatMap


