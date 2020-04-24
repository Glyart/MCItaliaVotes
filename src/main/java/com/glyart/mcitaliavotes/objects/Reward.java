package com.glyart.mcitaliavotes.objects;

import com.glyart.mcitaliavotes.utils.RewardExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor @Getter
public class Reward {
    
    private final int votes;
    private final List<String> rewards;
    
    public void executeReward(Player player) {
        for (String reward : rewards)
            RewardExecutor.execute(player, reward);
    }
    
}
