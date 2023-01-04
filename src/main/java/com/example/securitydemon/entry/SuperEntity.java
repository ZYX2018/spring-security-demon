package com.example.securitydemon.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import lombok.Data;

import java.util.Date;

@Data
public class SuperEntity {

    @TableId(type = IdType.ASSIGN_ID)
    @IsKey
    @Column
    private String id;
    /**
     * 创建时间
     */
    @Column(name = "create_time",comment = "创建时间") // name指定数据库字段名，comment为备注
    private Date createTime;
    /**
     * 最后修改时间
     */
    @Column(name = "update_time",comment = "最后修改时间")
    private Date updateTime;

}
