// IMediaCallBack.aidl
package com.amt.aidl;

interface IMediaCallBack {
    void onBtMusicCallBack(int func, int data);
    void onRadioCallBack(int func, int data);
    void onAudioCallBack(int func, int data);
    void onVideoCallBack(int func, int data);
    void onImageCallBack(int func, int data);
    void onOtherCallBack(int func, int data);
}
