package com.aarete.pi.config;

import com.aarete.pi.bean.ClaimLineBean;
import com.aarete.pi.bean.User;
import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.helper.Helper;
import com.aarete.pi.service.ClaimService;
import com.aarete.pi.service.MasterTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.aarete.pi.helper.ClaimServiceHelper.getClaimLineEntities;

@Profile("dev")
@Component
public class CommandLineTaskExecutor implements CommandLineRunner {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private MasterTableService masterTableService;

    @Override
    public void run(String... args) {
        try {
            List<ClaimLineBean> claimLineBeanList = Helper.dummySaveClaimlineData(10000000, 10, "Temp1234", "mpalla@aarete.com");
            List<ClaimLineEntity> claimLineEntities = getClaimLineEntities(claimLineBeanList);
            claimService.saveClaimLineList(claimLineEntities);
            masterTableService.addUser(getUsers());
        } catch (Exception e) {
        }
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(getUser("blella@aarete.com", "Babu Lella", "AARETE_USER"));
        users.add(getUser("csingh@aarete.com", "Chandan Singh", "AARETE_USER"));
        users.add(getUser("mpalla@aarete.com", "Muniswamy Palla", "AARETE_USER"));
        users.add(getUser("vbhaktwarti@aarete.com", "Vimal Bhaktwarti", "AARETE_USER"));
        users.add(getUser("akadam@aarete.com", "Anup Kadam", "AARETE_USER"));
        users.add(getUser("vjadhav@aarete.com", "Vinod Jadhav", "AARETE_USER"));
        users.add(getUser("rmarbate@aarete.com", "Rameshwar Marbate", "AARETE_USER"));
        users.add(getUser("schaware@aarete.com", "Saurabh Chaware", "AARETE_USER"));
        users.add(getUser("vrahangdale@aarete.com", "Vikas Rahangdale", "AARETE_USER"));
        return users;
    }
    private User getUser(String userID, String userName, String type) {
        User user = new User();
        user.setUserId(userID);
        user.setUserName(userName);
        user.setUserType(type);
        return user;
    }
}