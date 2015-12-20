package net.flyingfat.common.biz.xip;

import java.util.ArrayList;
import java.util.List;

import net.flyingfat.common.biz.MessageCode;
import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;


@SignalCode(messageCode = MessageCode.MSG_CODE_FOR_REPORT_ADS_ACTION_REQ)
public class ReportActionReq extends BaseXipRequest {

    @ByteField(index = 0, description = "uid")
    private String                uid;
  
    @ByteField(index = 1, description = "orderId")
    private Integer               orderId ;
  
    @ByteField(index = 2, description = "id")
    private Long                  id ;
  
    @ByteField(index = 3, description = "list")
    private ArrayList<String>           list ;



	public String getUid() {
	    return uid;
	}
	
	public void setUid(String uid) {
	    this.uid = uid;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}
	
	
  

}
