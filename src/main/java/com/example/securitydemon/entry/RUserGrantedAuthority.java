package com.example.securitydemon.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("security_r_user_authority")
@Table(name = "security_r_user_authority",comment = "用户-权限-关联表")
public class RUserGrantedAuthority extends SuperEntity implements Serializable {

    @Column(name = "user_id",comment = "用户主键",length = 100,isNull = false)
    private String userId;

    @Column(name = "authority_id",comment = "权限主键",length = 100,isNull = false)
    private String authorityId;

}
