package com.webank.webase.node.mgr.deploy.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.webank.webase.node.mgr.deploy.entity.TbChain;
import org.springframework.stereotype.Repository;

@Repository
public interface TbChainMapper {

    @Select({
            "select id,chain_name,chain_desc,version,encrypt_type,chain_status,run_type,create_time,modify_time,webase_sign_addr from tb_chain where chain_name = #{chainName,jdbcType=VARCHAR}"
    })
    TbChain getByChainName(@Param("chainName") String chainName);

    @Update({"update tb_chain set chain_status = #{newStatus},modify_time = #{modifyTime}  where id = #{chainId} and chain_status != #{newStatus}"})
    int updateChainStatus(@Param("chainId") int chainId, @Param("modifyTime") Date modifyTime, @Param("newStatus") byte newStatus );


    @Select({
            "select count(id) from tb_chain ",
    })
    int countChain();
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_chain
     *
     * @mbg.generated
     */
    @Delete({
        "delete from tb_chain where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(@Param("id") Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_chain
     *
     * @mbg.generated
     */
    @InsertProvider(type=TbChainSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT currval(id)", keyProperty="id", before=false, resultType=Integer.class)
    int insertSelective(TbChain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_chain
     *
     * @mbg.generated
     */
    @Select({
            "select id,chain_name,chain_desc,version,encrypt_type,chain_status,run_type,create_time,modify_time,webase_sign_addr from tb_chain where id = #{id,jdbcType=INTEGER}"
    })
    TbChain selectByPrimaryKey(@Param("id") Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_chain
     *
     * @mbg.generated
     */
    @UpdateProvider(type=TbChainSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(@Param("record") TbChain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_chain
     *
     * @mbg.generated
     */
    //TODO 无调用 psql不支持<script> 直接注释
    //@Options(useGeneratedKeys = true,keyProperty="id",keyColumn = "id")
    //@Insert({
    //"<script>",
    //    "insert into tb_chain (chain_name, ",
    //    "chain_desc, version, ",
    //    "encrypt_type, chain_status, root_dir,run_type, ",
    //    "create_time, modify_time,webase_sign_addr)",
    //    "values<foreach collection=\"list\" item=\"detail\" index=\"index\" separator=\",\">(#{detail.chainName,jdbcType=VARCHAR}, ",
    //    "#{detail.chainDesc,jdbcType=VARCHAR}, #{detail.version,jdbcType=VARCHAR}, ",
    //    "#{detail.encryptType,jdbcType=SMALLINT}, #{detail.chainStatus,jdbcType=SMALLINT},#{detail.rootDir,jdbcType=VARCHAR}, " +
    //            "#{detail.runType,jdbcType=SMALLINT},#{detail.webaseSignAddr,jdbcType=VARCHAR}",
    //    "#{detail.createTime,jdbcType=TIMESTAMP}, #{detail.modifyTime,jdbcType=TIMESTAMP})</foreach></script>"
    //})
    //int batchInsert(java.util.List<TbChain> list);
}