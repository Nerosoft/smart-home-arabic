package nero_soft.com.nero_soft.ViewHolder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import nero_soft.com.nero_soft.R;
import nero_soft.com.nero_soft.ServiceConnection;
import nero_soft.com.nero_soft.animation.MyAinm;

/**
 * Created by root on 21/01/18.
 */


public class SmartHome
        extends RecyclerView.Adapter<SmartHome.ViewHolder> {
    public SmartHome() {
    }

    private final TypedValue mTypedValue = new TypedValue();
    private LayoutInflater Inflater;
    private ViewGroup Container;
    FrameLayout frameLayout;
    public RecyclerView recyclerView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FrameLayout Frag, FrgMainRow, PointerH, PointerM;

        public LinearLayout LayOutFront, LayOutBack,
                Lay_click_led, FragLayTimeOn, FragLayTimeSetting;


        public ImageView SetTimer, ButSetTimeroff,
                LedOn, LedOff, But_Back_Time, ButCloseSetTimer,
                ButRunSetTimer, ButCloseTime,
                ButPlush, ButPlusm, ButMinush, ButMinusm, ButStateOnOff, ButRefreshTime;

        public TextView Text_HM, TextAllM, TextNameLedOnOf,
                TextNameLedTimer, TextStateOnOff;

        int degH = 0, degM = 5;
        String StateOfTime = "on";
        int OldHight;

        // كونستراكرتور لاجل تعريف كل العناصر للعرض في الريسيكل
        public ViewHolder(View view) {
            super(view);
            mView = view;
            this.OldHight = this.mView.getLayoutParams().height;
            Frag = view.findViewById(R.id.framcontainer);
            FrgMainRow = view.findViewById(R.id.frg_main_row);
            LayOutFront = view.findViewById(R.id.lay_out_front);
            LayOutBack = view.findViewById(R.id.lay_out_back);
            Lay_click_led = view.findViewById(R.id.lay_click_led);
            FragLayTimeOn = view.findViewById(R.id.frag_lay_time_on);
            FragLayTimeSetting = view.findViewById(R.id.frag_lay_time_setting);
            SetTimer = view.findViewById(R.id.butSetTimer);
            ButSetTimeroff = view.findViewById(R.id.butSetTimeroff);
            LedOn = view.findViewById(R.id.butLedOn);
            LedOff = view.findViewById(R.id.butLedOff);
            But_Back_Time = view.findViewById(R.id.but_Back_Time);
            ButCloseSetTimer = view.findViewById(R.id.but_close_set_timer);
            ButRefreshTime = view.findViewById(R.id.but_refresh_time);
            ButRunSetTimer = view.findViewById(R.id.but_run_set_timer);
            ButCloseTime = view.findViewById(R.id.but_close_time);

            TextNameLedOnOf = view.findViewById(R.id.text_name_led_on_of);
            TextNameLedTimer = view.findViewById(R.id.text_name_led_timer);
            //       TextStopWatchTimer = view.findViewById(R.id.text_stop_watch_timer);

            ButPlush = view.findViewById(R.id.but_plush);
            ButPlusm = view.findViewById(R.id.but_plusm);
            ButMinush = view.findViewById(R.id.but_minush);
            ButMinusm = view.findViewById(R.id.but_minusm);
            PointerH = view.findViewById(R.id.pointer_h);
            PointerM = view.findViewById(R.id.pointer_m);
            Text_HM = view.findViewById(R.id.text_h_m);
            TextAllM = view.findViewById(R.id.text_all_m);

            TextStateOnOff = view.findViewById(R.id.text_state_on_off);
            ButStateOnOff = view.findViewById(R.id.but_state_on_off);

            //    ButSetNewTime = view.findViewById(R.id.but_set_new_time);

        }

        public ViewHolder(View view, int Position) {
            super(view);
            mView = view;
            switch (Position) {
                case 1:
                    Lay_click_led = view.findViewById(R.id.lay_click_led);//---
                    LedOn = view.findViewById(R.id.butLedOn);//---
                    LedOff = view.findViewById(R.id.butLedOff);//---
                    return;

                case 2:
                    Frag = view.findViewById(R.id.framcontainer);//----
                    SetTimer = view.findViewById(R.id.butSetTimer);//------
                    ButSetTimeroff = view.findViewById(R.id.butSetTimeroff);//----
                    TextNameLedTimer = view.findViewById(R.id.text_name_led_timer);//---
                    LayOutBack = view.findViewById(R.id.lay_out_back);
                    LayOutFront = view.findViewById(R.id.lay_out_front);
                    FrgMainRow = view.findViewById(R.id.frg_main_row);
                    FragLayTimeSetting = view.findViewById(R.id.frag_lay_time_setting);
                    FragLayTimeOn = view.findViewById(R.id.frag_lay_time_on);
                    return;
            }
//
//            FrgMainRow = view.findViewById(R.id.frg_main_row);
//
//
//
//
//

//
//            But_Back_Time = view.findViewById(R.id.but_Back_Time);
//            ButCloseSetTimer = view.findViewById(R.id.but_close_set_timer);
//            ButRunSetTimer = view.findViewById(R.id.but_run_set_timer);
//            ButCloseTime = view.findViewById(R.id.but_close_time);
//
//            TextNameLedOnOf = view.findViewById(R.id.text_name_led_on_of);
//
//            TextStopWatchTimer = view.findViewById(R.id.text_stop_watch_timer);
//
//            ButPlush = view.findViewById(R.id.but_plush);
//            ButPlusm = view.findViewById(R.id.but_plusm);
//            ButMinush = view.findViewById(R.id.but_minush);
//            ButMinusm = view.findViewById(R.id.but_minusm);
//            PointerH = view.findViewById(R.id.pointer_h);
//            PointerM = view.findViewById(R.id.pointer_m);
//            Text_HM = view.findViewById(R.id.text_h_m);
//            TextAllM = view.findViewById(R.id.text_all_m);
//
//            TextStateOnOff = view.findViewById(R.id.text_state_on_off);
//            ButStateOnOff = view.findViewById(R.id.but_state_on_off);
//
//            ButSetNewTime = view.findViewById(R.id.but_set_new_time);

        }

        public void setUpSmartHome(final int position, final JSONObject jsonObjectled) {
            final MyAinm AnmiRotat = new MyAinm();

            //Led":[{"id":0,"name":"Living Room1","state":"of","led":"4","ledinterval":"0","refinterval":"0","statetime":"of","STL":"of","pir":0}

            try {
                if (jsonObjectled.getString("visibility").contentEquals("0")) {
                    MyAinm.fade(new MyAinm.ClickAction() {
                        @Override
                        public void click() {
                            mView.setVisibility(View.GONE);
                            mView.getLayoutParams().height = 0;
                        }
                    }, mView, 500, 1f, 0);

                } else {
                    if (this.mView.getVisibility() == View.GONE) {
                        this.mView.setAlpha(1f);
                        this.mView.setVisibility(View.VISIBLE);
                        this.mView.getLayoutParams().height = OldHight;
                    }
                }

                final String name_led = jsonObjectled.getString("name");
                final String led = jsonObjectled.getString("led");
                final String state = jsonObjectled.getString("state");
                final String STL = jsonObjectled.getString("STL");
                this.TextNameLedOnOf.setText(name_led);
                this.TextNameLedTimer.setText(name_led);

//--------------------------------------OFF AND ON MAN--------------------------------------------------------
                if (state.contentEquals("on")) {
                    this.LedOff.setVisibility(View.GONE);
                    this.LedOn.setVisibility(View.VISIBLE);
                } else {
                    this.LedOn.setVisibility(View.GONE);
                    this.LedOff.setVisibility(View.VISIBLE);
                }
//---------------------------------------TIME MAN--------------------------------------------------------------
                if (STL.contentEquals("on")) {
                    String ledinterval = jsonObjectled.getString("ledinterval");
                    String statetime = jsonObjectled.getString("statetime");
                    TextNameLedTimer.setText("Time Is " + ledinterval + " M " + statetime);
                    this.ButSetTimeroff.setVisibility(View.GONE);
                    this.SetTimer.setVisibility(View.VISIBLE);
                } else {
                    this.SetTimer.setVisibility(View.GONE);
                    this.ButSetTimeroff.setVisibility(View.VISIBLE);
                }
//--------------------------------------BUTTON MAN --------------------------------------------------------------
                SetTimer.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        MyAinm.setRotationWithVisiblity(null, Frag, AnmiRotat.Rotation + "Y", 500, 200,
                                FragLayTimeOn, FragLayTimeSetting, 0f, 90f, 270f, 360f);

                    }
                });

                But_Back_Time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (FragLayTimeSetting.getVisibility() == View.VISIBLE) {
                            MyAinm.setRotationWithVisiblity(null, Frag, AnmiRotat.Rotation + "Y", 500, 200,
                                    FragLayTimeSetting, FragLayTimeOn, 0f, 90f, 270f, 360f);

                        }
                    }
                });


//--------------------------------------------------------------------
// -------------------------------------------------------------------

                LedOff.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
//                        MyAinm.setRotationWithVisiblity(null, Lay_click_led, AnmiRotat.Rotation + "X", 300, 150,
//                                LedOff, LedOn, 0f, 90f, 270f, 360f);
                        ServiceConnection.sendLedMessage("led", "ledon" + led);

                    }
                });

                LedOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        MyAinm.setRotationWithVisiblity(null, Lay_click_led, AnmiRotat.Rotation + "X", 300, 150,
//                                LedOn, LedOff, 0f, 90f, 270f, 360f);
                        ServiceConnection.sendLedMessage("led", "ledof" + led);
                    }

                });


                ButSetTimeroff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyAinm.setRotationWithVisiblity(null, Frag, AnmiRotat.Rotation + "Y", 50, 200,
                                ButSetTimeroff, SetTimer, 0f, 90f, 270f, 360f);

                        MyAinm.setRotationWithVisiblity(null, FrgMainRow, AnmiRotat.Rotation + "X", 500, 200,
                                LayOutFront, LayOutBack, 0f, -90f, -270f, -360f);
                        //اظهار اعدادات لانشاء وقت
                    }
                });


                ButCloseSetTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyAinm.setRotationWithVisiblity(null, FrgMainRow, AnmiRotat.Rotation + "Y", 500, 200,
                                LayOutBack, LayOutFront, 0f, -90f, -270f, -360f);

                        MyAinm.setRotationWithVisiblity(null, Frag, AnmiRotat.Rotation + "Y", 1050, 200,
                                SetTimer, ButSetTimeroff, 0f, 90f, 270f, 360f);
                        //....... back
                    }
                });


                ButRunSetTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        MyAinm.setRotationWithVisiblity(null, FrgMainRow, AnmiRotat.Rotation + "Y", 500, 200,
//                                LayOutBack, LayOutFront, 0f, 90f, 270f, 360f);
                        //انشاء وقت لليد
                        try {
                            int total = (degH * 60) + degM;
                            ServiceConnection.Led.getJSONObject(position).put("refinterval", total);
                            ServiceConnection.sendTimeLedMessage("timeled", Integer.toString(position), total, StateOfTime, "on");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//.....
                    }
                });


                ButCloseTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        SetTimer.setVisibility(View.GONE);
//                        ButSetTimeroff.setVisibility(View.VISIBLE);
//                        MyAinm.setRotationWithVisiblity(null, Frag, AnmiRotat.Rotation + "X", 300, 150,
//                                FragLayTimeSetting, FragLayTimeOn, 0f, 90f, 270f, 360f);

                        ServiceConnection.MeassagDialog("Do You Want To Exit Time", mView, new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                ServiceConnection.sendTimeLedMessage("timeled", Integer.toString(position), 0, StateOfTime, "of");
                            }
                        });
//exit Time
                    }
                });

//----------------------------------------------------------------------------------
                ButPlusm.setOnClickListener(new View.OnClickListener() {
                    //   float deg = 0;

                    @Override
                    public void onClick(View view) {
                        degM = Math.round(PointerM.getRotation()) / 6;
                        if (degM != 60) {
                            PointerM.setRotation((degM * 6) + 6);
                            degM++;

                            String data[] = Text_HM.getText().toString().split(":");
                            if (degM < 10)
                                Text_HM.setText(data[0] + ":0" + degM);
                            else Text_HM.setText(data[0] + ":" + degM);
                            setTimeTextAllM();
                        }
                    }
                });

                ButMinusm.setOnClickListener(new View.OnClickListener() {
                    // float deg = 0;

                    @Override
                    public void onClick(View view) {
                        degM = Math.round(PointerM.getRotation()) / 6;
                        if (PointerM.getRotation() != 12) {
                            degM = Math.round(PointerM.getRotation()) / 6;
                            PointerM.setRotation((degM * 6) - 6);
                            degM--;
                            String data[] = Text_HM.getText().toString().split(":");
                            if (degM < 10)
                                Text_HM.setText(data[0] + ":0" + degM);
                            else Text_HM.setText(data[0] + ":" + degM);
                            setTimeTextAllM();
                        }
                    }
                });
                //=================================================HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH=============
                ButPlush.setOnClickListener(new View.OnClickListener() {
                    //float deg = 0;

                    @Override
                    public void onClick(View view) {
                        degH = Math.round(PointerH.getRotation()) / 30;
                        if (degH != 12) {
                            MyAinm.line(ButMinush, PointerH, view, degH, "+", 1000);
                            degH++;

                            String data[] = Text_HM.getText().toString().split(":");
                            if (degH < 10)
                                Text_HM.setText("0" + degH + ":" + data[1]);
                            else Text_HM.setText(degH + ":" + data[1]);
                            setTimeTextAllM();
                        }
                    }
                });

                ButMinush.setOnClickListener(new View.OnClickListener() {
                    //  float deg = 0;

                    @Override
                    public void onClick(View view) {
                        degH = Math.round(PointerH.getRotation()) / 30;
                        if (PointerH.getRotation() != 0) {
                            MyAinm.line(ButMinush, PointerH, view, degH, "-", 1000);
                            degH--;
                            String data[] = Text_HM.getText().toString().split(":");
                            if (degH < 10)
                                Text_HM.setText("0" + degH + ":" + data[1]);
                            else Text_HM.setText(degH + ":" + data[1]);
                            setTimeTextAllM();
                        }
                    }
                });

                ButStateOnOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextStateOnOff.getText().toString().contentEquals("ON")) {
                            ObjectAnimator.ofFloat(view, "rotationX", 0, 180f).setDuration(300).start();
                            TextStateOnOff.setText("OFF");
                            StateOfTime = "of";
                        } else {
                            ObjectAnimator.ofFloat(view, "rotationX", 180, 0f).setDuration(300).start();
                            TextStateOnOff.setText("ON");
                            StateOfTime = "on";
                        }
                    }
                });
                //-----------------------------------------------------------------------------------------------------------------------

//                ButSetNewTime.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        MyAinm.setRotationWithVisiblity(null, Frag, AnmiRotat.Rotation + "Y", 300, 100,
//                                FragLayTimeSetting, FragLayTimeOn, 0f, 90f, 270f, 360f);
//
//                        MyAinm.setRotationWithVisiblity(null, FrgMainRow, AnmiRotat.Rotation + "Y", 900, 200,
//                                LayOutFront, LayOutBack, 0f, 90f, 270f, 360f);
//                        //اعاده ادخال تيمر جديد مع ايقاف القديم
//                    }
//                });

                ButRefreshTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ServiceConnection.MeassagDialog("Do You Want To Refresh Time", mView, new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                try {
                                    int totalR = ServiceConnection.Led.getJSONObject(position).getInt("refinterval");
                                    ServiceConnection.sendTimeLedMessage("timeled", Integer.toString(position), totalR, StateOfTime, "on");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setActionOnAndOff(String state) {

            MyAinm AnmiRotat = new MyAinm();
            if (state.contentEquals("on")) {
                MyAinm.setRotationWithVisiblity(null, Lay_click_led, AnmiRotat.Rotation + "X", 300, 150,
                        LedOff, LedOn, 0f, 90f, 270f, 360f);
            } else {
                MyAinm.setRotationWithVisiblity(null, Lay_click_led, AnmiRotat.Rotation + "X", 300, 150,
                        LedOn, LedOff, 0f, 90f, 270f, 360f);
            }

        }

        public void setActionTimeOnAndOff(final String STL, final String text_info) {

            MyAinm AnmiRotat = new MyAinm();
            if (STL.contentEquals("on")) {
                //لو اليوزر بيحاول يدخل وقت وتم استلام في هذذيه اللحظه وقت من يوزر اخر
                if (this.LayOutBack.getVisibility() == View.VISIBLE)
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            TextNameLedTimer.setText(text_info);
                                                        }
                                                    }, FrgMainRow, AnmiRotat.Rotation + "Y",
                            500, 200, LayOutBack, LayOutFront, 0f, 90f, 270f, 360f);
                else if (FragLayTimeSetting.getVisibility() == View.VISIBLE || ButSetTimeroff.getVisibility() == View.GONE)
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            TextNameLedTimer.setText(text_info);
                                                        }
                                                    }, TextNameLedTimer, AnmiRotat.Alpha, 500, 200,
                            TextNameLedTimer, TextNameLedTimer, 1f, 0.0f, 0.0f, 1f);
                else
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            TextNameLedTimer.setText(text_info);
                                                        }
                                                    }, Frag, AnmiRotat.Rotation + "Y",
                            1050, 200, ButSetTimeroff, SetTimer, 0f, -90f, -270f, -360f);
//----------------------------------------------------------------------------------------------------------------------------------------------
               if(ButSetTimeroff.getVisibility() == View.VISIBLE)
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            TextNameLedTimer.setText(text_info);
                                                        }
                                                    }, Frag, AnmiRotat.Rotation + "Y",
                            1050, 200, ButSetTimeroff, SetTimer, 0f, -90f, -270f, -360f);

            } else {

                // لو انتها التيمر واليوزر فاتح اعدادات التيمر
                if (FragLayTimeSetting.getVisibility() == View.VISIBLE)
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            TextNameLedTimer.setText(text_info);
                                                            SetTimer.setVisibility(View.GONE);
                                                            ButSetTimeroff.setVisibility(View.VISIBLE);
                                                        }
                                                    }, Frag, AnmiRotat.Rotation + "Y",
                            500, 200, FragLayTimeSetting, FragLayTimeOn, 0f, 90f, 270f, 360f);

                else
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            TextNameLedTimer.setText(text_info);
                                                        }
                                                    }, Frag, AnmiRotat.Rotation + "Y",
                            1050, 200, SetTimer, ButSetTimeroff, 0f, 90f, 270f, 360f);
            }
        }

        public void setTimeTextAllM() {
            this.TextAllM.setText(degM + (degH * 60) + " Minutes");
        }

        @Override
        public String toString() {
            return super.toString();
        }

    }


    public SmartHome(Context context, LayoutInflater inflater, ViewGroup container) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.Inflater = inflater;
        this.Container = container;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.electronics, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.setUpSmartHome(position, ServiceConnection.Led.getJSONObject(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    } // end setup view

    @Override
    public int getItemCount() {
        return ServiceConnection.Led.length();
    }


    public void setupRecyclerView() { //-------------------------------set up contanir RecyclerView adaptor

        this.frameLayout = (FrameLayout) this.Inflater.inflate(R.layout.fragment_cheese_list, this.Container, false);
        this.recyclerView = this.frameLayout.findViewById(R.id.recyclerview);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        this.recyclerView.setAdapter(this);
    }

    public void setUpSettingView() {

//        FloatingActionButton vvv = this.frameLayout.findViewById(R.id.but_State_Of_Led);
//        final LinearLayout lay = this.frameLayout.findViewById(R.id.lay_State_Of_Led);
//        vvv.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(final View view) {
//
//                if (lay.getVisibility() == View.GONE) {
//                    ObjectAnimator.ofFloat(view, "rotationX", 0, 180f).setDuration(300).start();
//                    lay.setVisibility(View.VISIBLE);
//                    ObjectAnimator.ofFloat(lay, "scaleY", 0, 1f).setDuration(300).start();
//                } else {
//                    ObjectAnimator.ofFloat(view, "rotationX", 180, 0f).setDuration(300).start();
//                    ObjectAnimator ss = ObjectAnimator.ofFloat(lay, "scaleX", 1, 0f).setDuration(300);
//                    ss.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            lay.setVisibility(View.GONE);
//                            lay.setScaleX(1);
//                        }
//                    });
//                    ss.start();
//
//                }
//
//
//            }
//        });
    }

    public View getView() {
        return this.frameLayout;
    }

}