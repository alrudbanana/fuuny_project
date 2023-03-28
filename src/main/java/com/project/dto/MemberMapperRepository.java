package com.project.dto;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberMapperRepository {
	
		@Update("update member_table set member_phone = #{memberPhone}, member_profile_name = #{memberProfileName},member_level = #{memberLevel},member_interesting = #{memberInteresting} where member_id = #{memberId}")
	    void memberUpdate(MemberUpdateDto memberUpdateDTO);

	    @Update("update member_table set member_password = #{memberPassword} where member_id = #{memberId}")
	    void updatePassword(Long memberId, String memberPassword);

}
