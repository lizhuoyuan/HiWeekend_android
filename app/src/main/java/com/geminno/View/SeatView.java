package com.geminno.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.geminno.Bean.Seat;

import java.util.ArrayList;

/**
 * 1.View 测量2，事件3，细节，4，体会<br>
 * 网上教程大把。不过自己能体会才是自己的
 *
 * @ClassName: SeatView
 * @Description: 自定义View 实现拖动、缩放、
 * @author: XU
 * @date: 2015年10月29日 上午9:37:42
 */
public class SeatView extends View {
    private Paint paint;
    private Rect rect;
    /**
     * 座位宽度
     */
    private int seat_width;
    /**
     * 平均像素
     */
    private int avg_piexl;
    /**
     * X轴偏移量
     */
    private float offset_x = 0.0f;
    /**
     * Y轴偏移量
     */
    private float offset_y = 0.0f;
    /**
     * 第一次按下时X坐标
     */
    float temp_x = 0.0f;
    /**
     * 第一次按下时Y坐标
     */
    float temp_y = 0.0f;
    /**
     * 上一次X偏移量
     */
    float temp_offset_x = 0.0f;
    /**
     * 上一次Y偏移量
     */
    float temp_offset_y = 0.0f;
    /**
     * 整个View的宽度,
     */
    private int view_width;
    /**
     * 整个View的高度,
     */
    private int view_height;
    /**
     * 放大的比例
     */
    private float ratio = 1;
    /**
     * 按下时的斜边长度
     */
    private float old_hypotenuse;// 斜边
    /**
     * 触点个数
     */
    private int pointCount = 0;
    /**
     * 放大之前的平均像素，依次为基础进行放大
     */
    private int temp_avgPiexl;
    /**
     * see temp_avgPiexl
     */
    private int temp_seat_width;
    /**
     * X轴最大的偏移量，防止找不到
     */
    private int max_offset_x;
    /**
     * see max_offset_x
     */
    private int max_offset_y;
    /**
     * 座位集合，使用嵌套代替二维数组<br>
     * 内集合为列，外集合为行
     */
    private ArrayList<ArrayList<Seat>> seats;
    /**
     * 存储每行座位
     */
    private ArrayList<Seat> x_seats;
    /**
     * 座位对象
     *
     * @see Seat
     */
    private Seat seat;
    /**
     * 存储已经 选择的座位
     */
    private ArrayList<Seat> selected_seats;
    /**
     * x轴座位数
     */
    private int count_x = 0;
    /**
     * Y轴座位数
     */
    private int count_y = 0;

    public SeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        rect = new Rect();
        seats = new ArrayList<ArrayList<Seat>>();
    }

    public SeatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeatView(Context context) {
        this(context, null);
    }

    /**
     * @param x 座位列数，即X轴有多少座位
     * @param y 座位行数，即一列有多少座位
     * @Title: setSeat_X_Y
     * @Description: 设置座位的行列数
     * @Author XU
     */
    public void setSeat_X_Y(int x, int y) {
        this.count_x = x;
        this.count_y = y;

        for (int i = 0; i < y; i++) {
            seats.add(new ArrayList<Seat>());
        }
        for (int i = 0; i < seats.size(); i++) {
            for (int j = 0; j < x; j++) {
                seats.get(i).add(new Seat(j, i, Seat.OPTIONAL));
            }
        }
    }

    public void setSaledSeats(ArrayList<Seat> saledseats) {
        for (Seat seat : saledseats) {
            seats.get(seat.getCount_y() - 1).get(seat.getCount_x() - 1)
                    .setStatus(Seat.SALED);
        }
    }

    public void setSelectedSeats(ArrayList<Seat> selectedSeats) {
        this.selected_seats = selectedSeats;
        for (Seat seat : selectedSeats) {
            seats.get(seat.getCount_y()).get(seat.getCount_x())
                    .setStatus(Seat.SELECTED);
        }
    }

    /**
     * 1.View本身大小多少，这由onMeasure()决定<br>
     * 2.View在ViewGroup中的位置如何，这由onLayout()决定<br>
     * 3. 绘制View，onDraw()定义了如何绘制这个View。<br>
     * specMode:
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * @Title: onMeasure
     * @Description: 对默认的测量方法进行改写，
     * @see MeasureSpec MATCH_PARENT对应于EXACTLY，<br>
     * WRAP_CONTENT对应于AT_MOST，<br>
     * 其他情况也对应于EXACTLY， 它和MATCH_PARENT的区别在于size值不一样
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 这个值由高32位和低16位组成，
        // 高32位保存的值叫specMode，可以通过MeasureSpec.getMode()获取；
        // 低16位为specSize，同样可以由MeasureSpec.getSize()获取。
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 计算分别按X轴或Y轴的座位数计算的平均像素，
        int x_avg_piexl = widthSize / (count_x + 1);
        int y_avg_piexl = heightSize / (count_y + 1);
        // 取相对小的平均像素，适应不同屏幕都能完整能够显示
        avg_piexl = x_avg_piexl > y_avg_piexl ? y_avg_piexl : x_avg_piexl;

        seat_width = avg_piexl * 2 / 3;
        // 为行列的标记预留一个平均像素的空间
        view_width = avg_piexl * (count_x + 1);
        view_height = avg_piexl * (count_y + 1);
        // 设置偏移量，至少要显示一个座位
        max_offset_x = avg_piexl * (count_x);
        max_offset_y = avg_piexl * (count_y);

        setMeasuredDimension(view_width, view_height);
    }

    /**
     * getX()是表示Widget相对于自身左上角的x坐标<br>
     * 而getRawX()是表示相对于屏幕左上角的x坐标值
     *
     * @param event
     * @return true:监听器继续监听 false:只处理一个触摸事件
     * @Title: onTouchEvent
     * @Description: 拖动，缩放，点击选中的判断
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 多点触控要与MotionEvent.ACTION_MASK做与运算
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                old_hypotenuse = CountDistance(event);
                temp_avgPiexl = avg_piexl;
                temp_seat_width = seat_width;
                pointCount++;
                System.out.println("old_hypotenuse：" + old_hypotenuse);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointCount--;
                // System.out.println("触点数目：" + pointCount);

                break;
            case MotionEvent.ACTION_UP:
                pointCount = 0;

                SelectSeat(event);
                // System.out.println("触点数目：" + pointCount);

                break;

            case MotionEvent.ACTION_MOVE:
                if (pointCount == 1) {
                    offset_x = temp_offset_x + event.getX() - temp_x;
                    offset_y = temp_offset_y + event.getY() - temp_y;
                    if (offset_x > 0) {
                        offset_x = 0;
                    }
                    if (offset_y > 0) {
                        offset_y = 0;
                    }
                    if (offset_x + max_offset_x < 0) {
                        offset_x = -max_offset_x;
                    }
                    if (offset_y + max_offset_y < 0) {
                        offset_y = -max_offset_y;
                    }
                    // System.out
                    // .println("X:" + (offset_x > 0 ? "右" : "左") + offset_x);
                    // System.out
                    // .println("Y:" + (offset_y > 0 ? "下" : "上") + offset_y);
                } else if (pointCount == 2) {
                    float x = event.getX(0) - event.getX(1);
                    float y = event.getY(0) - event.getY(1);
                    // System.out.println("X距离差：" + x + "  Y距离差：" + y);

                    float new_ypotenuse = (float) Math.sqrt(x * x + y * y);
                    System.out.println("new_ypotenuse:" + new_ypotenuse);
                    if (Math.abs(new_ypotenuse - old_hypotenuse) > 10) {
                        ratio = new_ypotenuse / old_hypotenuse;
                        seat_width = (int) (temp_seat_width * ratio);
                        avg_piexl = (int) (temp_avgPiexl * ratio);
                        max_offset_x = avg_piexl * (count_x - 2);
                        max_offset_y = avg_piexl * (count_y - 2);
                        view_height = avg_piexl * (count_y + 1);
                        view_width = avg_piexl * (count_x + 1);
                    }
                    System.out.println("比例：" + ratio);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                // SelectSeat(event);
                pointCount = 1;
                temp_x = event.getX();
                temp_y = event.getY();
                temp_offset_x = offset_x;
                temp_offset_y = offset_y;
                // System.out.println("x:" + temp_x + " y:" + temp_y);
                // System.out.println("触点数目：" + pointCount);

                break;
            default:
                break;
        }

        invalidate();
        // boolean flag = super.onTouchEvent(event);
        return true;
    }

    /**
     * @param event
     * @Title: SelectSeat
     * @Description: 处理点击座位的事件
     * @Author XU
     */
    private void SelectSeat(MotionEvent event) {
        // 获取X,Y触点坐标
        float x = event.getX() - offset_x;
        float y = event.getY() - offset_y;
        // 判断触点是否超出范围
        if (x > view_width || y > view_height) {
            return;
        }
        // 计算触点所属块
        int x_count = (int) (x / avg_piexl);
        int y_count = (int) (y / avg_piexl);
        if (x_count <= 0 || y_count <= 0) {
            return;
        }
        // 触点在当前所在块的坐标
        float x_width = x - x_count * avg_piexl;
        float y_width = y - y_count * avg_piexl;
        String tostString = null;
        seat = seats.get(y_count - 1).get(x_count - 1);
        // 判断是否在当前块所在的座位范围内
        if (x_width < seat_width && y_width < seat_width) {
            switch (seat.getStatus()) {
                case Seat.OPTIONAL:
                    if (selected_seats.size() > 3) {
                        tostString = "最多四个座位";
                        break;
                    }
                    seat.setStatus(Seat.SELECTED);
                    selected_seats.add(seat);
                    tostString = "您选中了：" + seat;
                    break;
                case Seat.SALED:
                    tostString = "您不可以选择已经被售出的座位";
                    break;
                case Seat.SELECTED:
                    selected_seats.remove(seat);
                    seat.setStatus(Seat.OPTIONAL);
                    tostString = "您取消了：" + seat;
                    break;
                default:
                    break;
            }
            if (listener != null) {
                listener.onSeatClicked(tostString, selected_seats);
            }
        }
    }

    /**
     * @param event
     * @return
     * @Title: CountDistance
     * @Description: 计算距离
     * @Author XU
     */
    private float CountDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY() - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i <= count_y; i++) {
            if (i != 0)
                x_seats = seats.get(i - 1);
            for (int j = 0; j <= count_x; j++) {

                if (i == 0 && j != 0) {
                    paint.setTextSize(seat_width / 2);
                    paint.setColor(Color.RED);
                    paint.setAntiAlias(true);

                    canvas.drawText(j + "列", avg_piexl * j + offset_x,
                            seat_width + offset_y, paint);
                    continue;
                }
                if (j == 0 && i != 0) {
                    paint.setTextSize(seat_width / 2);
                    paint.setColor(Color.RED);
                    paint.setAntiAlias(true);

                    canvas.drawText((char) (64 + i) + "排",
                            (avg_piexl - seat_width) / 2 + offset_x, avg_piexl
                                    * i + offset_y + seat_width / 2, paint);
                    continue;
                }
                if (i != 0 && j != 0) {

                    seat = x_seats.get(j - 1);
                    initRect(j, i);
                    switch (seat.getStatus()) {
                        case Seat.OPTIONAL:
                            paint.setColor(Color.GRAY);
                            paint.setStyle(Style.STROKE);
                            break;
                        case Seat.SALED:
                            paint.setColor(Color.RED);
                            paint.setStyle(Style.FILL_AND_STROKE);
                            break;
                        case Seat.SELECTED:
                            paint.setColor(Color.GREEN);
                            paint.setStyle(Style.FILL_AND_STROKE);
                            break;
                        default:
                            break;
                    }
                    canvas.drawRect(rect, paint);
                }
            }
        }
    }

    // 初始化矩形
    private void initRect(int j, int i) {
        int left = (int) (avg_piexl * j + offset_x);
        int right = (int) ((avg_piexl * j + seat_width) + offset_x);
        int top = (int) (avg_piexl * i + offset_y);
        int bottom = (int) ((avg_piexl * i + seat_width) + offset_y);
        rect.set(left, top, right, bottom);
    }

    // 定义接口
    public interface SeatClickedListener {
        void onSeatClicked(String toast, ArrayList<Seat> seats);
    }

    private SeatClickedListener listener;

    public void setOnSeatClickedListener(SeatClickedListener listener) {
        this.listener = listener;
    }
}