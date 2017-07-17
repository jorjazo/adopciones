package io.rebelsouls.template.services;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;

import io.rebelsouls.core.users.User;

@Service
public class StorageService {

    @Value("${app.aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    private AmazonS3 client;

    public void store(User user, String path, File file) {
        String key = "/" + user.getId() + path;
        client.putObject(bucketName, key, file);
    }

    public List<String> listFilesForUser(User user) {
        String folderName = "/" + user.getUsername();
        ObjectListing results = client.listObjects(bucketName, folderName);
        return results.getObjectSummaries().stream().map(v -> v.getKey()).collect(Collectors.toList());
    }

}
