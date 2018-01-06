package io.github.lunarwatcher.jvmi18n.android;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

public class LoaderConfig {
    private LoadingMode mode;
    private Context ctx;

    private LoaderConfig(){

    }

    public void setMode(LoadingMode mode){
        if(mode == null){
            throw new NullPointerException();
        }
        this.mode = mode;
    }

    public LoadingMode getMode(){
        return mode;
    }

    public void setContext(Context ctx){
        if(ctx == null){
            throw new NullPointerException();
        }
        this.ctx = ctx;
    }

    public Context getContext(){
        return ctx;
    }

    public static class Builder{
        private LoadingMode mode;
        private Context ctx;

        public Builder loadingMode(@NotNull LoadingMode mode){
            this.mode = mode;
            return this;
        }

        public Builder context(@NotNull Context ctx){
            this.ctx = ctx;
            return this;
        }

        public LoaderConfig build(){
            if(mode == null) mode = LoadingMode.ASSETS;
            if(ctx == null) throw new NullPointerException("Context is null");

            LoaderConfig config = new LoaderConfig();
            config.setMode(mode);

            return config;
        }
    }
}
