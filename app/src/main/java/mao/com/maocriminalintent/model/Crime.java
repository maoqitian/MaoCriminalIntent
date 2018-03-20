package mao.com.maocriminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by maoqitian on 2018/2/23 0023.
 * Crime实例代表某种办公室陋习。
 * 布尔值用于表示陋习是否被解决
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private int mRequiresPolice;

    private String mSuspect;//嫌疑人姓名

    private int position;

    private String mSuspectContact;//联系嫌疑人电话

    public Crime(){
        this(UUID.randomUUID());
        /*mId=UUID.randomUUID();
        mDate=new Date();*/
    }

    public Crime(UUID uuid){
        mId=uuid;
        mDate=new Date();
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public int getmRequiresPolice() {
        return mRequiresPolice;
    }

    public void setmRequiresPolice(int mRequiresPolice) {
        this.mRequiresPolice = mRequiresPolice;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }
    public String getmSuspectContact() {
        return mSuspectContact;
    }

    public void setmSuspectContact(String mSuspectContact) {
        this.mSuspectContact = mSuspectContact;
    }
}
