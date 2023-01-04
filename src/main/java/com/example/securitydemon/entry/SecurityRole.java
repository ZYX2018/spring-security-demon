package com.example.securitydemon.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("security_role")
@Table(name = "security_role")
public class SecurityRole extends SuperEntity implements Serializable {
    @Column(name = "role_name",comment = "角色名称")
    private String roleName;
    @Column(name = "role_code",comment = "角色编码")
    private String roleCode;
    @Column(name = "description",comment = "角色描述")
    private String description;
}
