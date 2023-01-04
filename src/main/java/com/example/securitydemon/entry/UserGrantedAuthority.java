package com.example.securitydemon.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Data
@TableName("security_user_authority")
@Table(name = "security_user_authority")
public class UserGrantedAuthority  extends SuperEntity implements GrantedAuthority , Serializable {

    @Column(name = "authority",comment = "权限编码",length = 50)
    private String authority;
    @Column(name = "authority_description",comment = "权限描述")
    private String authorityDescription;
    @Column(name = "authority_class_path",comment = "资源类路径")
    private String authorityClassPath;

    @Override
    public String getAuthority() {
        return authority;
    }
}
