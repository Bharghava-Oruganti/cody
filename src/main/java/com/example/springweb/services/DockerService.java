package com.example.springweb.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DockerService {

    @Autowired
    private DockerClient dockerClient;
    private String local_path = "C:\\Users\\BHARGHAVA\\Desktop\\springweb\\src\\main\\java\\com\\example\\springweb\\files";
    private String container_path = "/files/";
    Volume volume = new Volume(container_path);
    public String createContainer(){
        try{
            CreateContainerResponse container = dockerClient.createContainerCmd("cpp_comp")
                    .withHostConfig(new HostConfig()
                            .withNetworkMode("bridge")
                            .withBinds(new Bind(local_path,volume)
                    ))
                    .exec();

            // starting of container with data
            dockerClient.startContainerCmd(container.getId())
                    .exec();
            // wait
            WaitContainerResultCallback resultCallback = new WaitContainerResultCallback();
            dockerClient.waitContainerCmd(container.getId()).exec(resultCallback);
            resultCallback.awaitCompletion();

            return "Success";
        }catch(Exception e){
            e.printStackTrace();
        }

        return "Failure in container creation";
    }
}
