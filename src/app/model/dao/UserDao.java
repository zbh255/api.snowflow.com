package app.model.dao;

import app.model.mapping.Custom;
import app.model.mapping.CustomShow;
import common.database.MysqlOrmDriver;
import common.errors.ErrorDefine;
import common.errors.Errors;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;
import org.jfaster.mango.operator.Mango;

// 用户实例的增删改查
public class UserDao {

    private Mango DbDriver;

    public UserDao() {
        this.DbDriver = MysqlOrmDriver.getCrudConnection();
    }

    // 注册用户
    public Errors UserLogin(Custom c) {
        LoginDao dao = this.DbDriver.create(LoginDao.class);
        // 查找数据库中是否有此用户
        Custom cc = dao.findByUserName(c.getcName());
        if (cc == null) {
            dao.add(c);
            return ErrorDefine.Ok;
        } else {
            return ErrorDefine.ErrUserNameExist;
        }
    }

    // 删除用户
    public Errors UserDelete(String uid) {
        LoginDao dao = this.DbDriver.create(LoginDao.class);
        Custom cc = dao.findByUserId(uid);
        if (cc != null) {
            dao.delete(uid);
            return ErrorDefine.Ok;
        } else {
            return ErrorDefine.ErrUserNameExist;
        }
    }

    // 更改用户密码
    public Errors UserUpdatePassword(String uid,String password) {
        LoginDao dao = this.DbDriver.create(LoginDao.class);
        // 用户存在且密码正确则更新
        Custom c = dao.findByCidAndCpassword(uid, password);
        if (c != null) {
            dao.update(c);
            return ErrorDefine.Ok;
        } else {
            return ErrorDefine.ErrUserNotFoundOrPasswordIncorrect;
        }
    }

    // 更新用户信息
    public Errors UserUpdateInfo(CustomShow cs) {
        UserInfoDao dao = this.DbDriver.create(UserInfoDao.class);
        // 查找用户是否存在
        CustomShow cs2 = dao.findByUserId(cs.getCid());
        if (cs2 != null) {
            cs2.setUserSex(cs.getUserSex());
            cs2.setUserAge(cs.getUserAge());
            dao.update(cs2);
            return ErrorDefine.Ok;
        } else {
            return ErrorDefine.ErrUserNotFound;
        }
    }

    // 用户登录查询
    // [0]为错误类型
    // [1]为查询的数据
    public Object[] UserSign(String userName,String password) {
        LoginDao dao = this.DbDriver.create(LoginDao.class);
        Custom c = dao.findByUserNameAndCpassword(userName, password);
        // 用户存在且密码正确则登录成功
        Object[] o = new Object[2];
        if (c != null) {
            o[0] = ErrorDefine.Ok;
            o[1] = c;
        } else {
            o[0] = ErrorDefine.ErrUserNotFoundOrPasswordIncorrect;
            o[1] = null;
        }
        return o;
    }

    // 根据Id查询用户的Token
    // [0]为错误类型
    // [1]为String类型的Token
    public Object[] searchUserToken(String uid) {
        LoginDao dao = this.DbDriver.create(LoginDao.class);
        Custom c = dao.findByUserId(uid);
        Object[] o = new Object[2];
        if (c != null) {
            o[0] = ErrorDefine.Ok;
            o[1] = c.getcToken();
        } else {
            o[0] = ErrorDefine.ErrUserNotFoundOrPasswordIncorrect;
            o[1] = null;
        }
        return o;
    }

    // 更新用户的Token
    public Errors updateUserToken(String uid,String token) {
        LoginDao dao = this.DbDriver.create(LoginDao.class);
        Custom c = dao.findByUserId(uid);
        if (c != null) {
            c.setcToken(token);
            int num = dao.update(c);
            // 未发生更改的情况
            if (num == 0) {
                return ErrorDefine.ErrDateBaseUpdate;
            }
            return ErrorDefine.Ok;
        } else {
            return ErrorDefine.ErrUserNotFound;
        }
    }

    @DB(table = Custom.DB_NAME)
    interface LoginDao extends CrudDao<Custom,String> {

        @SQL("select Cid, Cpassword from #table where Cid = :1 and Cpassword = :2")
        Custom findByCidAndCpassword(String cid,String cPassword);

        @SQL("select Cname, Cpassword from #table where Cid = :1 and Cpassword = :2")
        Custom findByUserNameAndCpassword(String userName,String cPassword);

        @SQL("delete from #table where Cid = :1")
        int deleteByCid(String cid);

        @SQL("select Cname from #table where Cname = :1")
        Custom findByUserName(String userName);

        @SQL("select Cid from #table where Cid = :1")
        Custom findByUserId(String uid);

    }

    @DB(table = CustomShow.DB_NAME)
    interface UserInfoDao extends CrudDao<CustomShow,String> {

        @SQL("select Cname, from #table where Cname = :1")
        CustomShow findByUserName(String userName);

        @SQL("select Cid, form #table where Cid = :1")
        CustomShow findByUserId(String uid);
    }
}
