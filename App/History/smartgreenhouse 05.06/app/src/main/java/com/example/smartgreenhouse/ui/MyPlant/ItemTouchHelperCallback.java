package com.example.smartgreenhouse.ui.MyPlant;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

enum ButtonsState{
    GONE,
    UP_VISIBLE,
    DOWN_VISIBLE
}

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperListener listener;
    private boolean swipeBack = false;
    private ButtonsState buttonsShowedState = ButtonsState.GONE;
    private static final float buttonHeight = 115;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipe_flags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int drag_flags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(drag_flags, swipe_flags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition());
    }

    //아이템을 터치하거나 스와이프하거나 뷰에 변화가 생길경우 불러오는 함수
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //아이템이 스와이프 됐을경우 버튼을 그려주기 위해서 스와이프가 됐는지 확인
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (buttonsShowedState != ButtonsState.GONE) {
                if (buttonsShowedState == ButtonsState.UP_VISIBLE) dY = Math.max(buttonHeight, dY);
                if (buttonsShowedState == ButtonsState.DOWN_VISIBLE)
                    dY = Math.min(-buttonHeight, dY);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            if (buttonsShowedState == ButtonsState.GONE) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        currentItemViewHolder = viewHolder;
        //버튼을 그려주는 함수
        drawButtons(c, currentItemViewHolder);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonHeightWithOutPadding = buttonHeight - 10;
        float corners = 20;
        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        buttonInstance = null;

           //아래쪽으로 스와이프 했을때 (위쪽에 버튼이 보여지게 될 경우)
        if (buttonsShowedState == ButtonsState.UP_VISIBLE) {
            RectF upButton = new RectF(itemView.getLeft() ,itemView.getTop()  , itemView.getRight(), itemView.getTop()+120);
            p.setColor(Color.BLUE);
            c.drawRoundRect(upButton, corners, corners, p);
            drawText("수정", c, upButton, p);
            buttonInstance = upButton;

            //위쪽으로 스와이프 했을때 (아래쪽에 버튼이 보여지게 될 경우)
        } else if (buttonsShowedState == ButtonsState.DOWN_VISIBLE) {
            RectF downButton = new RectF(itemView.getLeft() ,itemView.getBottom() -160  , itemView.getRight(), itemView.getBottom()-40);
            p.setColor(Color.RED);
            c.drawRoundRect(downButton, corners, corners, p);
            drawText("삭제", c, downButton, p);
            buttonInstance = downButton;
        }
    }
    //버튼의 텍스트 그려주기
    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 25;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);
        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dY < -buttonHeight) buttonsShowedState = ButtonsState.DOWN_VISIBLE;
                    else if (dY > buttonHeight) buttonsShowedState = ButtonsState.UP_VISIBLE;
                    if (buttonsShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ItemTouchHelperCallback.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                setItemsClickable(recyclerView, true);
                swipeBack = false;
                if (listener != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                    if (buttonsShowedState == ButtonsState.UP_VISIBLE) {
                        listener.onUpClick(viewHolder.getAdapterPosition(), viewHolder);
                    } else if (buttonsShowedState == ButtonsState.DOWN_VISIBLE) {
                        listener.onDownClick(viewHolder.getAdapterPosition(), viewHolder);
                    }
                }
                buttonsShowedState = ButtonsState.GONE;
                currentItemViewHolder = null;
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }
}

