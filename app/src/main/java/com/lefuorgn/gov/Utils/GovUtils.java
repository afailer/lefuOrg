package com.lefuorgn.gov.Utils;

import android.graphics.Color;

/**
 * Created by liuting on 2017/1/9.
 */

public class GovUtils {
    public static GovUtils govUtils=null;
    public static GovUtils getInstance(){
        if(govUtils==null){
            govUtils=new GovUtils();
        }
        return govUtils;
    }
    String[] colorSets={"#FE4678","#47CBFF","#FFC856","#2ACACD","#9DD758","#FF9229","#34C1E2","#FE4678"};
    public int getColor(int no){
        return Color.parseColor(colorSets[no%colorSets.length]);
    }
}
