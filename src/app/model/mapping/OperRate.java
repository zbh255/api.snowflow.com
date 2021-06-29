package app.model.mapping;

import org.jfaster.mango.annotation.AutoGenerated;
import org.jfaster.mango.annotation.Column;
import org.jfaster.mango.annotation.ID;
import org.jfaster.mango.mapper.AbstractRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class OperRate extends AbstractRowMapper<OperRate> {

    public static final String DB_NAME = "operrate";

    // 账单的id
    @ID
    @Column("Oid")
    private String operRateId;

    // 账单的源地址id
    @Column("Cid")
    private String sourceId;

    // 账单的接受地址id
    @Column("Sid")
    private String destId;

    // 账单的状态
    @Column("state")
    private int state;

    // 账单备注
    @Column("note")
    private String note;

    // 账单的创建时间
    @Column("Otime")
    private Date createTime;

    // 计数金钱，相对于源地址
    @Column("Omoney")
    private double mountMoney;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getMountMoney() {
        return mountMoney;
    }

    public void setMountMoney(double mountMoney) {
        this.mountMoney = mountMoney;
    }

    public String getOperRateId() {
        return operRateId;
    }

    public void setOperRateId(String operRateId) {
        this.operRateId = operRateId;
    }

    @Override
    public OperRate mapRow(ResultSet rs, int i) throws SQLException {
        OperRate o = new OperRate();
        o.setSourceId(rs.getString("Cid"));
        o.setOperRateId(rs.getString("Oid"));
        o.setDestId(rs.getString("Sid"));
        o.setCreateTime(rs.getTime("Otime"));
        o.setNote(rs.getString("note"));
        o.setMountMoney(rs.getDouble("Omoney"));
        o.setState(rs.getInt("state"));
        return o;
    }
}