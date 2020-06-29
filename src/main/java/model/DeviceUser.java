package model;

import org.bson.Document;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class DeviceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    public int enrollid;

    @Column
    public int backupnum;

    @Column
    public int admin;

    @Column
    public String name;

    @Column(length = 2000)
    public String record;


    public void setId(int id) {
        this.id = id;
    }

    public void setEnrollid(int enrollid) {
        this.enrollid = enrollid;
    }

    public void setBackupnum(int backupnum) {
        this.backupnum = backupnum;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getId() {
        return id;
    }

    public int getEnrollid() {
        return enrollid;
    }

    public int getBackupnum() {
        return backupnum;
    }

    public int getAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public String getRecord() {
        return record;
    }


    public boolean isAdmin(){
        return admin == 1;
    }

    public boolean isRfid(){
        return backupnum == 11;
    }
    public boolean isFingerprint(){
        return backupnum <= 9 && backupnum >= 0;
    }

    public boolean isPass(){
        return backupnum == 10;
    }

    public Document toDoc(){
        return  new Document()
                .append("enrollid", enrollid)
                .append("name", name)
                .append("backupnum", backupnum)
                .append("admin", admin)
                .append("record", record);
    }

    public String toJson(){
        return toDoc().toJson();
    }


}
