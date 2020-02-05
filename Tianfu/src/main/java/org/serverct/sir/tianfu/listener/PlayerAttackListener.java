package org.serverct.sir.tianfu.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.config.PlayerDataManager;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.data.Talent;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.util.LocaleUtil;
import org.serverct.sir.tianfu.util.PlaceholderUtil;

import java.util.Random;

public class PlayerAttackListener implements Listener{

    private Talent lightning = TalentManager.getInstance().getTalent(TalentType.LIGHTNING);
    private Talent imprisonment = TalentManager.getInstance().getTalent(TalentType.IMPRISONMENT);
    private Talent healthRefill = TalentManager.getInstance().getTalent(TalentType.HEALTHREFILL);
    private Talent damage = TalentManager.getInstance().getTalent(TalentType.DAMAGE);
    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    private Random random = new Random();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {

        if(event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            PlayerData victimData = PlayerDataManager.getInstance().getPlayerData(victim.getName());

            if(victim.getHealth() < (victim.getMaxHealth() * 0.1)) {
                if((boolean) healthRefill.getExecutor().execute(victimData)) {
                    victim.setHealth(victim.getMaxHealth());
                    victim.sendMessage(
                            PlaceholderUtil.check(
                                    locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Talent", "HealthRefill"),
                                    TalentType.HEALTHREFILL,
                                    victimData
                            )
                    );
                }
            }
        }

        if(event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Entity victim = event.getEntity();
            PlayerData attackerData = PlayerDataManager.getInstance().getPlayerData(attacker.getName());

            if(victim != null) {
                if((boolean) lightning.getExecutor().execute(attackerData)) {
                    victim.getLocation().getWorld().strikeLightning(victim.getLocation());
                    attacker.sendMessage(
                            PlaceholderUtil.check(
                                    locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Talent", "Lightning"),
                                    TalentType.LIGHTNING,
                                    attackerData
                            )
                    );
                }

                if(victim instanceof Player) {
                    Player victimPlayer = (Player) victim;
                    if(attackerData.getLevel().get(TalentType.IMPRISONMENT) > 0) {
                        locale.debug("攻击者拥有禁锢天赋, 攻击者数据: " + attackerData.toString());
                        locale.debug("攻击者: " + attacker.toString());
                        locale.debug("受害者: " + victimPlayer.toString());
                        if(random.nextInt(100) <= 10) {
                            locale.debug("中奖.");
                            victimPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) imprisonment.getExecutor().execute(attackerData) * 20, 10), true);
                            attacker.sendMessage(
                                    PlaceholderUtil.check(
                                            locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Talent", "Imprisonment.Cast"),
                                            TalentType.IMPRISONMENT,
                                            attackerData
                                    )
                            );
                            victimPlayer.sendMessage(
                                    PlaceholderUtil.check(
                                            locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Talent", "Imprisonment.Victim"),
                                            TalentType.IMPRISONMENT,
                                            attackerData
                                    )
                            );
                        }
                    }
                }
            }
            event.setDamage(event.getDamage() + (int) damage.getExecutor().execute(attackerData));
        }
    }
}
