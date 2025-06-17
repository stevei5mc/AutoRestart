package cn.stevei5mc.autorestart.gui;

import cn.lanink.gamecore.form.element.ResponseElementButton;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowCustom;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.lanink.gamecore.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.form.element.*;
import cn.stevei5mc.autorestart.AutoRestartPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsConfig {
    private static final AutoRestartPlugin main = AutoRestartPlugin.getInstance();

    private SettingsConfig() {
        throw new RuntimeException("Error");
    }

    public static void configSettings(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(lang.translateString("form_title_config_set"));
        simple.setContent(lang.translateString("form_config_set_description"));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_config_set_base")).onClicked(SettingsConfig::configBaseSettings));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_config_set_show")).onClicked(SettingsConfig::configShowSettings));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_config_set_command")).onClicked(SettingsConfig::configCmdSettings));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_config_set_sound")).onClicked(SettingsConfig::configSoundSetting));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_back")).onClicked(Admin::sendMain));
        player.showFormWindow(simple);
    }

    // 基础设置
    public static void configBaseSettings(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        String timeUnitMin = lang.translateString("time_unit_type", lang.translateString("time_unit_minutes"));
        String timeUnitS = lang.translateString("time_unit_type", lang.translateString("time_unit_seconds"));
        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(lang.translateString("form_title_config_set_base"));
        custom.addElement(new ElementDropdown("default_language", main.getLanguages(), main.getLanguages().indexOf(main.getConfig().getString("default_language"))));
        custom.addElement(new ElementInput("restart_time  "+timeUnitMin,"",String.valueOf(main.getConfig().getInt("restart_time",180))));
        custom.addElement(new ElementInput("tips_time  "+timeUnitS,"",String.valueOf(main.getConfig().getInt("tips_time",30))));
        custom.addElement(new ElementLabel(lang.translateString("form_config_set_base_label_local_language")));
        custom.addElement(new ElementToggle("local_language_flies",main.getConfig().getBoolean("local_language_flies",false)));
        custom.addElement(new ElementLabel(lang.translateString("form_config_set_base_label_auto_update_language")));
        custom.addElement(new ElementToggle("auto_update_language_files",main.getConfig().getBoolean("auto_update_language_files",false)));
        custom.addElement(new ElementLabel(lang.translateString("form_config_set_base_label_ignore_vote_remainder")));
        custom.addElement(new ElementToggle("ignore_remainder_time",main.getConfig().getBoolean("ignore_remainder_time",false)));
        custom.addElement(new ElementToggle("kick_player",main.getConfig().getBoolean("kick_player",true)));
        custom.onClosed(SettingsConfig::configSettings);
        custom.onResponded((form, player1) -> {
            try {
                main.getConfig().set("default_language", form.getDropdownResponse(0).getElementContent());
                main.getConfig().set("restart_time",Integer.parseInt(form.getInputResponse(1)));
                main.getConfig().set("tips_time",Integer.parseInt(form.getInputResponse(2)));
                main.getConfig().set("local_language_flies", form.getToggleResponse(4));
                main.getConfig().set("auto_update_language_files", form.getToggleResponse(6));
                main.getConfig().set("ignore_remainder_time",form.getToggleResponse(8));
                main.getConfig().set("kick_player",form.getToggleResponse(9));
                configSave();
                configBaseSettings(player);
            }catch (NumberFormatException e) {
                player.sendMessage(lang.translateString("error_input_parameter"));
            }

        });
        player.showFormWindow(custom);
    }

    // 显示设置
    public static void configShowSettings(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(lang.translateString("form_title_config_set_show"));
        custom.addElement(new ElementToggle("show_title",main.getConfig().getBoolean("show_title",true)));
        custom.addElement(new ElementToggle("show_tip",main.getConfig().getBoolean("show_tip",true)));
        custom.addElement(new ElementToggle("prompt_voting_status",main.getConfig().getBoolean("prompt_voting_status",true)));
        custom.addElement(new ElementStepSlider("prompt_type", Arrays.asList("ActionBar","tip","popup"),main.getConfig().getInt("prompt_type",0)));
        custom.addElement(new ElementInput("message_prefix", "", main.getConfig().getString("message_prefix","§l§bAutoRestart §r§7>> ")));
        custom.onClosed(SettingsConfig::configSettings);
        custom.onResponded((form, player1) -> {
            main.getConfig().set("show_title",form.getToggleResponse(0));
            main.getConfig().set("show_tip",form.getToggleResponse(1));
            main.getConfig().set("prompt_voting_status",form.getToggleResponse(2));
            main.getConfig().set("prompt_type",form.getStepSliderResponse(3).getElementID());
            main.getConfig().set("message_prefix", form.getInputResponse(1));
            configSave();
            configShowSettings(player);
        });
        player.showFormWindow(custom);
    }

    // 命令设置
    public static void configCmdSettings(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(lang.translateString("form_title_config_set_command"));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_config_set_command_view")).onClicked(player1 -> {
            StringBuilder globalCmd = new StringBuilder();
            if (!main.getConfig().getStringList("commands.global").isEmpty()) {
                for (String cmd : main.getConfig().getStringList("commands.global")) {
                    globalCmd.append("   - ").append(cmd).append("\n");
                }
            }
            StringBuilder playerCmd = new StringBuilder();
            if (!main.getConfig().getStringList("commands.player").isEmpty()) {
                for (String cmd : main.getConfig().getStringList("commands.player")) {
                    playerCmd.append("   - ").append(cmd).append("\n");
                }
            }
            AdvancedFormWindowSimple cmdList = new AdvancedFormWindowSimple(lang.translateString("form_title_config_set_command_view"));
            cmdList.setContent(
                    "global commands:\n"
                    +globalCmd
                    +"\nplayer commands:\n"
                    +playerCmd
            );
            cmdList.addButton(new ResponseElementButton(lang.translateString("form_button_back")).onClicked(SettingsConfig::configSettings));
            player1.showFormWindow(cmdList);
        }));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_config_set_command_create")).onClicked(SettingsConfig::createCmd));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_config_set_command_delete")).onClicked(SettingsConfig::deleteCmd));
        simple.addButton(new ResponseElementButton(lang.translateString("form_button_back")).onClicked(SettingsConfig::configSettings));
        player.showFormWindow(simple);
    }

    // 创建命令
    public static void createCmd(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowCustom createCmd = new AdvancedFormWindowCustom(lang.translateString("form_title_config_set_command_create"));
        createCmd.addElement(new ElementLabel(lang.translateString("form_config_set_command_create_label_tip")));
        createCmd.addElement(new ElementInput("创建的命令", "say Hello world or say Hello @p&con"));
        createCmd.addElement(new ElementToggle("global / player",false));
        createCmd.onClosed(SettingsConfig::configSettings);
        createCmd.onResponded((form, player1) -> {
            String cmdName = form.getInputResponse(1);
            String cmdType = form.getToggleResponse(2)? "commands.player" : "commands.global";
            ArrayList<String> cmdList = new ArrayList<>(main.getConfig().getStringList(cmdType));
            if (cmdName.equals("") || cmdName.isEmpty() || cmdList.contains(cmdName)) {
                player.sendMessage(lang.translateString("command_create_failed", cmdName));
            }else {
                player.sendMessage(lang.translateString("command_create_success",cmdName));
                cmdList.add(cmdName);
                main.getConfig().set(cmdType,cmdList);
                configSave();
            }
        });
        player.showFormWindow(createCmd);
    }

    // 删除命令
    public static void deleteCmd(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowCustom deleteCmd = new AdvancedFormWindowCustom(lang.translateString("form_title_config_set_command_delete"));
        ArrayList<String> globalCmd = new ArrayList<>(main.getConfig().getStringList("commands.global"));
        ArrayList<String> playerCmd = new ArrayList<>(main.getConfig().getStringList("commands.player"));
        deleteCmd.addElement(new ElementToggle(lang.translateString("form_config_set_command_delete_confirm_choose")));
        deleteCmd.addElement(new ElementDropdown("global command",globalCmd));
        deleteCmd.addElement(new ElementToggle(lang.translateString("form_config_set_command_delete_confirm_choose")));
        deleteCmd.addElement(new ElementDropdown("player command",playerCmd));
        deleteCmd.onClosed(SettingsConfig::configSettings);
        deleteCmd.onResponded((form, player1) -> {
            AdvancedFormWindowCustom delCmdList = new AdvancedFormWindowCustom(lang.translateString("form_title_config_set_command_delete"));
            delCmdList.addElement(new ElementLabel(lang.translateString("form_config_set_command_delete_confirm_description")));
            if (form.getToggleResponse(0)) {
                delCmdList.addElement(new ElementToggle(lang.translateString("form_config_set_command_delete_confirm_again")));
                delCmdList.addElement(new ElementLabel("Global command:\n   - "+form.getDropdownResponse(1).getElementContent()+"\n"));
            }
            if (form.getToggleResponse(2)) {
                delCmdList.addElement(new ElementToggle(lang.translateString("form_config_set_command_delete_confirm_again")));
                delCmdList.addElement(new ElementLabel("Player command:\n   - "+form.getDropdownResponse(3).getElementContent()+"\n"));
            }
            delCmdList.onClosed(SettingsConfig::deleteCmd);
            delCmdList.onResponded((form1, player2) -> {
                if (form.getToggleResponse(0) && form1.getToggleResponse(1)) {
                    globalCmd.remove(form.getDropdownResponse(1).getElementContent());
                    main.getConfig().set("commands.global",globalCmd);
                }
                boolean ruleA = form.getToggleResponse(0) && form.getToggleResponse(2) && form1.getToggleResponse(3);
                boolean ruleB = !form.getToggleResponse(0) && form.getToggleResponse(2) && form1.getToggleResponse(1);
                if (ruleA || ruleB) {
                    playerCmd.remove(form.getDropdownResponse(3).getElementContent());
                    main.getConfig().set("commands.player",playerCmd);
                }
                if (form.getToggleResponse(0) || form.getToggleResponse(2)) {
                    configSave();
                }
            });
            player1.showFormWindow(delCmdList);
        });
        player.showFormWindow(deleteCmd);
    }

    // 音效设置
    public static void configSoundSetting(@NotNull Player player) {
        Language lang = AutoRestartPlugin.getInstance().getLang(player);
        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(lang.translateString("form_title_config_set_sound"));
        custom.addElement(new ElementToggle("play_sound", main.getConfig().getBoolean("play_sound", true)));
        custom.addElement(new ElementInput("sound name","",main.getConfig().getString("sound.name","random.toast")));
        custom.addElement(new ElementInput("sound volume","",String.valueOf(main.getConfig().getDouble("sound.volume",1.0))));
        custom.addElement(new ElementInput("sound pitch","",String.valueOf(main.getConfig().getDouble("sound.pitch",0.5))));
        custom.onClosed(SettingsConfig::configSettings);
        custom.onResponded((form, player1) -> {
            try {
                main.getConfig().set("play_sound",form.getToggleResponse(0));
                main.getConfig().set("sound.name", form.getInputResponse(1));
                double volumeValue = Double.parseDouble(form.getInputResponse(2));
                if (volumeValue > 0 && volumeValue <= 1) {
                    main.getConfig().set("sound.volume", volumeValue);
                }else {
                    throw new  NumberFormatException();
                }
                double pitchValue = Double.parseDouble(form.getInputResponse(3));
                if (pitchValue > 0 && pitchValue <= 1) {
                    main.getConfig().set("sound.pitch", pitchValue);
                }else {
                    throw new NumberFormatException();
                }
                configSave();
                configSoundSetting(player);
            }catch (NumberFormatException e) {
                player.sendMessage(lang.translateString("error_input_parameter"));
            }
        });
        player.showFormWindow(custom);
    }

    public static void configSave() {
        main.getConfig().save();
        main.reload();
    }
}