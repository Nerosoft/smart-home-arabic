package nero_soft.com.nero_soft.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by root on 20/01/18.
 */

public class MyAinm {

    public String Rotation = "rotation";
    public String Alpha = "alpha";
    String Scale = "scale";


    public static void setRotationWithVisiblity(final ClickAction action, View view, String distance, int duration_one, final int duration_tow, final View gon_view, final View visi_view, float... values) {

        AnimatorSet set = new AnimatorSet();

        ObjectAnimator offl = ObjectAnimator.ofFloat(view, distance, values[0], values[1]).setDuration(duration_one);
        offl.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                gon_view.setVisibility(View.GONE);
                visi_view.setVisibility(View.VISIBLE);
                if (action != null)
                    action.click();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

        set.play(offl).before(ObjectAnimator.ofFloat(view, distance, values[2], values[3]).setDuration(duration_tow));
        set.start();
    }


    public static void line(final View mun ,final View PointerH ,final View click ,float degH,String state ,int duration) {

        float new_deg=(state=="+")?(degH*30)+30:(degH*30)-30;
        ObjectAnimator anm= ObjectAnimator.ofFloat(PointerH,"Rotation",degH*30,new_deg).setDuration(duration);
        anm.addListener(new AnimatorListenerAdapter() {
           @Override
            public void onAnimationStart(Animator animation){
               click.setEnabled(false);
               mun.setEnabled(false);
            }
            @Override
            public void onAnimationEnd(Animator animation){
                click.setEnabled(true);
                mun.setEnabled(true);
            }
        });anm.start();
    }


    public static void setMargin(final View frameLayout) {

        final int newLeftMargin = 500;
        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
                params.rightMargin = (int) (newLeftMargin * interpolatedTime);
                frameLayout.setLayoutParams(params);

            }
        };
        a.setDuration(500); // in ms
        frameLayout.startAnimation(a);

    }

    public static void fade(final ClickAction action , final View view, final int duration, float... values){
        ObjectAnimator offl = ObjectAnimator.ofFloat(view, "alpha", values[0], values[1]).setDuration(duration);
        offl.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                action.click();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        offl.start();
    }

    public interface ClickAction {
        void click();
    }


}
