# FlowLayout 流式布局

# 一、实现思路
#### 1、通过重写onMeasure()方法，获取自己的宽高和childView的宽高。
- 1.1 通过调用childView的measure()方法，然后让子view自己测量。这其中涉及MeasureSpec.UNSPECIFIED等模式，但是这些操作都是定式的，只需要调用**measureChildWithMargins()** 方法，就帮我们完成了各个模式下让子view.measure()的方法了。
**注意：这有个坑，调用这个方法时，必须先重写generateLayoutParams方法，然后返回MarginLayoutParams()**
- 1.2 测量完之后，直接可以调用，childView.getMeasureWidth()/Height()来获取childView的宽高。
- 1.3 然后根据childView的宽高，来计算当前childView应该在什么位置，也就是上下左右的坐标，拿到这个数据之后，我们用List<Rect>可以将它存起来，方便onLayout用。 
- 1.4 并根据childView的总高度和最大宽度，可以通过setMeasuredDimension()设置给自己。以便如果自己也在其他布局中的时候，给父view调用。
  
#### 2、通过重写onLayout()方法，来对子View进行布局。把他们的坐标位置通过childView.layout()方法设置给子view
- 2.1 把List<Rect>存储的子view数据，遍历然后通过childView.layout()设置给子view
  
#### 3、具体onMeasure()的计算方法。
- 3.1 通过遍历measureChildWithMargins()测量每一个childview宽高。
- 3.2 通过吧childView的width值加起来lineWidthUsed 对比 当前view的宽度tagLayoutSpecWidthSize = MeasureSpec.getSize(widthMeasureSpec);  
3.2.1 如果lineWidthUsed 小于 tagLayoutSpecWidthSize，那么就直接存到List<Rect>中    
3.2.2 如果lineWidthUsed 大于 tagLayoutSpecWidthSize，那么就错一行，把高度加高，然后把新高度当成起始点。这时需要再调用measureChildWithMargins一次，因为上一次调用时，如果当前view的宽度不够它全展开，那么它会自己压缩自己，所以我们需要把lineWidthUsed设为0，再调一次。

