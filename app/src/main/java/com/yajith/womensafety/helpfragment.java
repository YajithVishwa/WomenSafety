package com.yajith.womensafety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class helpfragment extends Fragment {
    String para;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fagement_help,container,false);
        para="Violence against women in India refer to physical or sexual violence committed against Indian women, typically by a man. Common forms of violence against women in India include acts such as domestic abuse, sexual assault, and murder. In order to be considered violence against women, the act must be committed solely because the victim is female. Most typically, these acts are committed by men as a result of the long-standing gender inequalities present in the country.";
        para=para+"So we created this app to protect woman and protect them toward unwanted society evils";
        para+="Thank you for your love and support by Team Noob Coders";
        textView=v.findViewById(R.id.help);
        textView.setText(para);
        return v;
    }
}
