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
filter函数的参数是一个Predicate<T>接口,它表示从T到boolean的一个函数
当想要对流中的值进行某种转换时,可以使用map方法,并传递一个执行该转换的函数,例如:
```
    Stream<String> lowerCaseWords = words.stream().map(String::toLowerCase);
```
也可以使用lambda表达式:
```
    Stream<String> lowerCaseWords = words.stream().map(s -> s.toLowerCase());
```
flatMap方法:将一个包含流的流摊平为一个流

## 裁剪流和连接流
stream.limit(n)会返回一个截断的流,在n个元素之后结束,如果原来流的长度不足n,则返回原来的流
而stream.skip(n)则是丢弃前n个元素
静态方法Stream.concat(stream1,stream2)则会将两个流连接起来

## distinct
stream.distinct()方法会将流中原来的元素按照同样的顺序剔除重复元素

## sorted
```
    Stream<String> longestFirst = words.stream().sorted(Comparator.comparing(String::length).reversed());
```
要求其中的元素实现Comparable接口

## peek
对于调试流处理语句非常方便,每次获取一个元素时,都会调用peek中的函数

## 约简
常见约简操作:
```
    Optional<T> max(Comparator<? super T> comparator);
    Optional<T> min(Comparator<? super T> comparator);
    Optional<T> findFirst()
    Optional<T> finaAny()  //产生任意一个元素
    boolean anyMatch(Predicate<? super T> predicate)
    boolean allMatch(Predicate<? super T> predicate)
    boolean noneMatch(Predicate<? super T> predicate)
```

## Optional
常见用法:
1. 在没有值时给出默认值,或者按照给定函数计算默认值,或者抛出异常
```
    String result = optionalStr.orElse("");
    String result = optionalStr.orElseGet(() -> Locale.getDefault().getDisplayName());
    String result = optionalStr.orElseThrow(IllegalStateException::new);
```
2. 只有在值存在的情况下才消费该值
```
    opetionalValue.ifPresent(v -> process(v));
```

使用get方法获取值,当optional存在值时会获得其中的元素,如果没有值,则抛出NoSuchElementException异常

创建Optional对象:
Optional.of(result)或者Optional.empty()
注意:of方法的参数如果为null会抛出异常,当不确定是不是null时应使用Optional.ofNullable(result)

## 收集结果
```
    Iterator<T> iterator() //产生一个获取流中元素的迭代器
    void forEach(Consumer<? super T> action)
    Object[] toArray()
    <R,A> R collect(Collector<? super T,A,R> collector)
    getCount(),getMax(),getMin(),getAverage()
```

```
    List<String> result = stream.collect(Collectors.toList());
    TreeSet<String> result = stream.collect(Collectors.toCollection(TreeSet::new))
```

## 收集Map
```
    Map<Integer,Person> idToPerson = people.collect(Collectors.toMap(Person::getId,Function.identity()));
```
如果多个元素具有相同的键,那么就会存在冲突,收集器将会抛出IllegalStateException异常.可以通过提供第三个函数参数来定义冲突后的行为,以确定键的值
```
    Map<String,String> languageNames = locales.collect(Collectors.toMap(Locale::getDisplayLanguage,l -> l.getDisplayLanguage(l), (existingValue,newValue) -> existingValue));
```

## 群组和分区
groupingBy
```
    Map<String,List<Locale>> countryToLocales = locales.collect(Collectors.groupingBy(Locale::getCountry));
API:
    static<T,K> Collector<T,?,Map<K,List<t>>> groupingBy(Function<? super T, ? extends K> classifier)
    static<T> Collector<T,?,Map<boolean,List<T>>> partitionBy(Predicate<? super T> predicate)
```

## 下游收集器
```
    toSet():
    Map<String,List<Locale>> countryToLocales = locales.collect(Collectors.groupingBy(Locale::getCountry, toSet()));
    counting():
    Map<String,List<Locale>> countryToLocales = locales.collect(Collectors.groupingBy(Locale::getCountry, counting()));
    summingInt/Long/Double(),maxBy(),minBy():
    Map<String,Integer> map = cities.collect(groupingBy(City::getState, summingInt(City::getPopulation)));
    Map<String,Integer> map = cities.collect(groupingBy(City::getState, maxBy(Comparator.comparing(City::getPopulation))));

```

## reduce操作:
列表求和:
```
    List<Integer> values = ...
    Optional<Integer> sum = values.stream().reduce((x,y) -> x+y);
 或
    Optional<Integer> sum = values.stream().reduce(Integer::sum);
```

## 基本类型流
IntStream,LongStream等
之前都是用Stream<Integer>,使用基本类型流可以避免装箱
IntStream.of或者Arrays.stream

## 并行流
```
    words.parallelStream();
    Stream.of(wordArray),parallel();
```






