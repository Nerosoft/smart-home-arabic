package nero_soft.com.nero_soft.interFace;

import org.json.JSONObject;


public interface Setting {

    public boolean run();


    public static interface blockAndKill{
        public void runKill();
        public void runBlock(String operator);
    }

    public static interface SetPost{
        public void PostExecute(JSONObject result);
        public void Cansle();

    }

    public static interface GetPost{

        public interface getSettingLedInfo{
            public void  PostExecuteSettingLedInfo(JSONObject result);
        }
    }


}
