package com.mobile2016.backend.service;
import com.mobile2016.backend.model.User;
import com.mobile2016.backend.mapper.UserMapper;
import com.mobile2016.common.utils.LoggerUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class UploadService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 腾讯云上传文件服务
     */
    private final String TengXun_Bucket="storage2017";
    private final long TexngXun_APPID=1254182311;
    private final String IMAG_BASE_URL = "http://storage2017-1254182311.file.myqcloud.com/image_2017/";
    private final String  TengXun_ScrectId="AKIDXu2vZyX4lESQ8ariOt87qJWTCgwx3nJa";
    private final String  TengXun_ScrectKey= "92g4C3e193i8JEuKUO1OVMxINCl48xXr";

    private Credentials cred = new Credentials(TexngXun_APPID, TengXun_ScrectId, TengXun_ScrectKey);
    private ClientConfig clientConfig = new ClientConfig();
    private COSClient cosClient = new COSClient(clientConfig, cred);



    @CachePut(key="'UserId'+#p0.id")
    public  User updatehead(User user){
        userMapper.updateUser(user);
        return   userMapper.findUserByMobile(user);

    }

    public  void uploadFile(String fileName,byte[] bytes,User user){

        clientConfig.setRegion("sh");

        UploadFileRequest uploadFileRequest = new UploadFileRequest(TengXun_Bucket, "/image_2017/"+fileName
                , bytes);
        String uploadFileRet = cosClient.uploadSingleFile(uploadFileRequest);

        if(uploadFileRet!=null&&!uploadFileRequest.equals("")) {

            String  str=uploadFileRet.toString();

            JSONObject object = new JSONObject(str);

            LoggerUtil.W("------->"+object.getInt("code"));

                int code=object.getInt("code");

                if(code==0){
                    user.setAvatar(IMAG_BASE_URL+fileName);
                    updatehead(user);
                }

        }


    }
}
