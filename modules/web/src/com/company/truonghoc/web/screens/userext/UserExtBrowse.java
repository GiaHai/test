package com.company.truonghoc.web.screens.userext;

import com.aspose.words.FieldDatabase;
import com.company.truonghoc.entity.Donvi;
import com.company.truonghoc.entity.UserExt;
import com.company.truonghoc.service.DulieuUserService;
import com.company.truonghoc.service.SearchedService;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.app.security.user.browse.UserBrowser;
import com.haulmont.cuba.gui.app.security.user.browse.UserRemoveAction;
import com.haulmont.cuba.gui.app.security.user.resetpasswords.ResetPasswordsDialog;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.security.app.SecurityScopesService;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.role.RoleDefinition;
import com.haulmont.cuba.security.role.RoleDefinitionsJoiner;
import com.haulmont.cuba.security.role.RolesService;
import org.apache.commons.lang3.BooleanUtils;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

public class UserExtBrowse extends AbstractLookup {
    @Inject
    protected GroupTable<UserExt> userExtsTable;
    @Inject
    protected DataSupplier dataSupplier;
    @Inject
    protected Metadata metadata;
    @Inject
    protected RolesService rolesService;
    @Named("userExtsTable.remove")
    protected RemoveAction userExtsTableRemove;
    @Inject
    protected GroupDatasource<UserExt, UUID> userExtsDs;
    @Named("userExtsTable.excel")
    protected ExcelAction userExtsTableExcel;
    @Inject
    protected Security security;
    @Named("userExtsTable.edit")
    protected EditAction userExtsTableEdit;
    @Named("userExtsTable.copySettings")
    protected Action userExtsTableCopySettings;
    @Named("userExtsTable.create")
    protected CreateAction userExtsTableCreate;
    @Named("userExtsTable.copy")
    protected Action userExtsTableCopy;
    @Named("userExtsTable.changePasswAtLogon")
    protected Action userExtsTableChangePasswAtLogon;
    @Named("userExtsTable.changePassw")
    protected Action userExtsTableChangePassw;
    @Named("userExtsTable.resetRememberMe")
    protected Action userExtsTableResetRememberMe;
    @Inject
    protected UserManagementService userManagementService;
    @Inject
    protected PopupButton additionalActionsBtn;
    @Inject
    protected SecurityScopesService securityScopesService;
    @Inject
    protected LookupField<Donvi> donviField;
    @Inject
    protected SearchedService searchedService;
    @Inject
    protected DulieuUserService dulieuUserService;
    @Inject
    protected UserSession userSession;
    @Inject
    protected UiComponents uiComponents;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        final boolean hasPermissionsToCreateUsers =
                security.isEntityOpPermitted(User.class, EntityOp.CREATE);

        final boolean hasPermissionsToUpdateUsers =
                security.isEntityOpPermitted(User.class, EntityOp.CREATE);

        final boolean hasPermissionsToCreateSettings =
                security.isEntityOpPermitted(UserSetting.class, EntityOp.CREATE);

        // no selection
        userExtsTableCopySettings.setEnabled(false);
        userExtsTableChangePassw.setEnabled(false);
        userExtsTableChangePasswAtLogon.setEnabled(false);

        userExtsTableResetRememberMe.setEnabled(security.isEntityOpPermitted(RememberMeToken.class, EntityOp.DELETE));

        userExtsDs.addItemChangeListener(e -> {
            if (userExtsTable.getSelected().size() > 1) {
                userExtsTableCopy.setEnabled(false);
                userExtsTableChangePassw.setEnabled(false);
            } else {
                userExtsTableCopy.setEnabled(hasPermissionsToCreateUsers && e.getItem() != null);
                userExtsTableChangePassw.setEnabled(hasPermissionsToUpdateUsers && e.getItem() != null);
            }

            userExtsTableChangePasswAtLogon.setEnabled(hasPermissionsToUpdateUsers && e.getItem() != null);
            userExtsTableCopySettings.setEnabled(hasPermissionsToCreateSettings && e.getItem() != null);
        });

        userExtsDs.addCollectionChangeListener(e -> {
            if (e.getDs().getState() == Datasource.State.VALID) {
                userExtsTableCopy.setEnabled(hasPermissionsToCreateUsers && e.getDs().getItem() != null);
                userExtsTableChangePassw.setEnabled(hasPermissionsToUpdateUsers && e.getDs().getItem() != null);
                userExtsTableChangePasswAtLogon.setEnabled(hasPermissionsToUpdateUsers && e.getDs().getItem() != null);
                userExtsTableCopySettings.setEnabled(hasPermissionsToCreateSettings && e.getDs().getItem() != null);
            }
        });

        RemoveAction removeAction = new UserRemoveAction(userExtsTable, userManagementService);
        userExtsTable.addAction(removeAction);

        additionalActionsBtn.addAction(userExtsTableCopySettings);
        additionalActionsBtn.addAction(userExtsTableChangePassw);
        additionalActionsBtn.addAction(userExtsTableChangePasswAtLogon);
        additionalActionsBtn.addAction(userExtsTableResetRememberMe);

        if (WindowParams.MULTI_SELECT.getBool(getFrame().getContext())) {
            userExtsTable.setMultiSelect(true);
        }

//        initTimeZoneColumn();
//        initGroupColumn();
        if (rolesService.getRolesPolicyVersion() == 2) {
            initShowEffectiveRoleActions();
        }

        donviField.setOptionsList(searchedService.loaddonvi());
        if (!dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi().getDonvitrungtam()) {
            donviField.setValue(dulieuUserService.timdovi(userSession.getUser().getLogin()).getLoockup_donvi());
            donviField.setEditable(false);
            excuteSearch();
        }
    }

    private void initShowEffectiveRoleActions() {
        final boolean hasPermissionToOpenRoleEditor =
                security.isScreenPermitted("sec$Role.edit")
                        && security.isEntityOpPermitted(Role.class, EntityOp.READ);

        List<BaseAction> actions = new ArrayList<>();
        Collection<SecurityScope> securityScopes = securityScopesService.getAvailableSecurityScopes();
        if (securityScopes.size() == 1) {
            String caption = getMessage("showEffectiveRole");
            actions.add(new ShowEffectiveRoleAction(securityScopes.iterator().next().getName(), caption));
        } else {
            for (SecurityScope securityScope : securityScopes) {
                String caption = formatMessage("showEffectiveRoleForScope", securityScope.getLocName());
                actions.add(new ShowEffectiveRoleAction(securityScope.getName(), caption));
            }
        }
        for (BaseAction action : actions) {
            userExtsTable.addAction(action);
            additionalActionsBtn.addAction(action);
            action.addEnabledRule(() -> hasPermissionToOpenRoleEditor);
        }
    }


//    private void initGroupColumn() {
//        userExtsTable.addGeneratedColumn("group", entity -> {
//            String groupName = entity.getGroup() != null ? entity.getGroup().getName() : entity.getGroupNames();
//            return new Table.PlainTextCell(groupName);
//        });
//    }

    //    private void initTimeZoneColumn() {
//        userExtsTable.addGeneratedColumn("timeZone", entity -> {
//            String timeZone = null;
//            if (Boolean.TRUE.equals(entity.getTimeZoneAuto())) {
//                timeZone = messages.getMainMessage("timeZone.auto");
//            } else if (entity.getTimeZone() != null) {
//                timeZone = entity.getTimeZone();
//            }
//            return new Table.PlainTextCell(timeZone);
//        });
//    }
    protected class ShowEffectiveRoleAction extends ItemTrackingAction {

        protected String securityScope;
        protected String actionCaption;

        public ShowEffectiveRoleAction(String securityScope, String caption) {
            super("showEffectiveRole_" + securityScope);
            this.securityScope = securityScope;
            this.actionCaption = caption;
        }

        @Override
        public void actionPerform(Component component) {
            showEffectiveRole(securityScope);
        }

        @Override
        public String getCaption() {
            return actionCaption;
        }
    }

    private void showEffectiveRole(String securityScope) {
        UserExt user = userExtsTable.getSingleSelected();
        if (user == null) return;
        Role role = buildJoinedRole(user, securityScope);
        Map<String, Object> params = new HashMap<>();
        //permissionsLoaded = true indicates that permissions tabs in the role editor should not reload permissions,
        //but should use permissions from the Role instance
        params.put("permissionsLoaded", true);
        openEditor(role, WindowManager.OpenType.THIS_TAB, params);
    }

    private Role buildJoinedRole(UserExt user, String securityScope) {
        Collection<RoleDefinition> roleDefinitions = rolesService.getRoleDefinitionsForUser(user);
        List<RoleDefinition> roleDefinitionsForScope = roleDefinitions.stream()
                .filter(roleDefinition -> securityScope.equals(roleDefinition.getSecurityScope()))
                .collect(Collectors.toList());
        RoleDefinition joinedRoleDefinition = RoleDefinitionsJoiner.join(roleDefinitionsForScope);
        Role joinedRole = rolesService.transformToRole(joinedRoleDefinition);
        joinedRole.setSecurityScope(securityScope);
        joinedRole.setName(getMessage("effectiveRoleName"));
        joinedRole.setDescription(formatMessage("effectiveRoleDescription", metadata.getTools().getInstanceName(user)));
        return joinedRole;
    }

    public void copy() {
        Set<UserExt> selected = userExtsTable.getSelected();
        if (!selected.isEmpty()) {
            User selectedUser = selected.iterator().next();
            selectedUser = dataSupplier.reload(selectedUser, "user.edit");
            UserExt newUser = metadata.create(UserExt.class);
            if (selectedUser.getUserRoles() != null) {
                List<UserRole> userRoles = new ArrayList<>();
                Role role = null;
                String roleName = null;
                for (UserRole oldUserRole : selectedUser.getUserRoles()) {
                    if (oldUserRole.getRole() != null) {
                        role = dataSupplier.reload(oldUserRole.getRole(), "_local");
                        if (BooleanUtils.isTrue(role.getDefaultRole())) {
                            continue;
                        }
                    } else if (oldUserRole.getRoleName() != null) {
                        RoleDefinition roleDefinition = rolesService.getRoleDefinitionByName(oldUserRole.getRoleName());
                        if (roleDefinition.isDefault()) {
                            continue;
                        }
                        roleName = oldUserRole.getRoleName();
                    }
                    UserRole newUserRole = metadata.create(UserRole.class);
                    newUserRole.setUser(newUser);
                    newUserRole.setRole(role);
                    newUserRole.setRoleName(roleName);
                    userRoles.add(newUserRole);
                }
                newUser.setUserRoles(userRoles);
            }
            newUser.setGroup(selectedUser.getGroup());
            newUser.setGroupNames(selectedUser.getGroupNames());
            AbstractEditor editor = openEditor("sec$User.edit", newUser, WindowManager.OpenType.THIS_TAB,
                    ParamsMap.of("initCopy", true));
            editor.addCloseListener(actionId -> {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    userExtsDs.refresh();
                }
                userExtsTable.focus();
            });
        }
    }

    public void copySettings() {
        Set<UserExt> selected = userExtsTable.getSelected();
        if (!selected.isEmpty()) {
            Window copySettingsWindow = openWindow(
                    "sec$User.copySettings",
                    WindowManager.OpenType.DIALOG,
                    ParamsMap.of("users", selected)
            );
            copySettingsWindow.addCloseListener(actionId ->
                    userExtsTable.focus()
            );
        }
    }

    public void changePassword() {
        UserExt selectedUser = userExtsTable.getSingleSelected();
        if (selectedUser != null) {
            Window changePasswordDialog = openWindow("sec$User.changePassword",
                    WindowManager.OpenType.DIALOG,
                    ParamsMap.of("user", selectedUser));

            changePasswordDialog.addCloseListener(actionId -> {
                if (COMMIT_ACTION_ID.equals(actionId)) {
                    userExtsDs.updateItem(dataSupplier.reload(selectedUser, userExtsDs.getView()));
                }
                userExtsTable.focus();
            });
        }
    }

    public void changePasswordAtLogon() {
        if (!userExtsTable.getSelected().isEmpty()) {
            ResetPasswordsDialog resetPasswordsDialog = (ResetPasswordsDialog) openWindow("sec$User.resetPasswords", WindowManager.OpenType.DIALOG);
            resetPasswordsDialog.addCloseListener(actionId -> {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    boolean sendEmails = resetPasswordsDialog.getSendEmails();
                    boolean generatePasswords = resetPasswordsDialog.getGeneratePasswords();
                    Set<UserExt> users = userExtsTable.getSelected();
                    resetPasswords(users, sendEmails, generatePasswords);
                }
                userExtsTable.focus();
            });
        }
    }

    private void resetPasswords(Set<UserExt> users, boolean sendEmails, boolean generatePasswords) {
        List<UUID> usersForModify = new ArrayList<>();
        for (User user : users) {
            usersForModify.add(user.getId());
        }

        if (sendEmails) {
            Integer modifiedCount = userManagementService.changePasswordsAtLogonAndSendEmails(usersForModify);
            userExtsDs.refresh();

            showNotification(String.format(getMessage("resetPasswordCompleted"), modifiedCount),
                    NotificationType.HUMANIZED);
        } else {
            Map<UUID, String> changedPasswords = userManagementService.changePasswordsAtLogon(usersForModify, generatePasswords);

            if (generatePasswords) {
                Map<User, String> userPasswords = new LinkedHashMap<>();
                for (Map.Entry<UUID, String> entry : changedPasswords.entrySet()) {
                    userPasswords.put(userExtsDs.getItem(entry.getKey()), entry.getValue());
                }

                Window newPasswordsWindow = openWindow("sec$User.newPasswords", WindowManager.OpenType.DIALOG,
                        ParamsMap.of("passwords", userPasswords));
                newPasswordsWindow.addCloseListener(actionId ->
                        userExtsTable.focus()
                );
            } else {
                showNotification(
                        formatMessage("changePasswordAtLogonCompleted", changedPasswords.size()),
                        NotificationType.HUMANIZED);
            }
            userExtsDs.refresh();
        }
    }

    public void resetRememberMe() {

        if (userExtsTable.getSelected().isEmpty()) {
            showOptionDialog(
                    getMessage("resetRememberMeTitle"),
                    getMessage("resetRememberMeQuestion"),
                    MessageType.CONFIRMATION,
                    new Action[]{
                            new BaseAction("actions.ResetAll")
                                    .withCaption(getMessage("actions.ResetAll"))
                                    .withHandler(event -> resetRememberMeAll()),

                            new DialogAction(DialogAction.Type.CANCEL, Action.Status.PRIMARY)
                    }
            );
        } else {
            showOptionDialog(
                    getMessage("resetRememberMeTitle"),
                    getMessage("resetRememberMeQuestion"),
                    MessageType.CONFIRMATION,
                    new Action[]{
                            new BaseAction("actions.ResetOptionSelected")
                                    .withCaption(getMessage("actions.ResetOptionSelected"))
                                    .withHandler(event -> resetRememberMe(userExtsTable.getSelected())),

                            new BaseAction("actions.ResetOptionAll")
                                    .withCaption(getMessage("actions.ResetOptionAll"))
                                    .withHandler(event -> resetRememberMeAll()),

                            new DialogAction(DialogAction.Type.CANCEL, Action.Status.PRIMARY)
                    }
            );
        }
    }

    private void resetRememberMe(Set<UserExt> users) {
        List<UUID> usersForModify = users.stream()
                .map(BaseUuidEntity::getId)
                .collect(Collectors.toList());

        userManagementService.resetRememberMeTokens(usersForModify);

        showNotification(getMessage("resetRememberMeCompleted"), NotificationType.HUMANIZED);
    }

    private void resetRememberMeAll() {
        userManagementService.resetRememberMeTokens();

        showNotification(getMessage("resetRememberMeCompleted"), NotificationType.HUMANIZED);
    }

    public void timkiem() {
        excuteSearch();
    }

    private void excuteSearch() {
        Object donvi = donviField.getValue();

        String query = returnQuery(donvi);
        userExtsDs.setQuery(query);
        userExtsDs.commit();
        userExtsDs.refresh();
    }

    private String returnQuery(Object donvi) {
        String query = "select e from truonghoc_UserExt e ";
        String where = " where 1=1 ";

        if (donvi != null) {
            where += " and e.loockup_donvi.tendonvi like '%" + donviField.getValue().getTendonvi() + "%' ";
        }
        query = query + where;
        return query;
    }

    public Component donvichinh(UserExt entity) {
        Label label = uiComponents.create(Label.class);
        if (entity.getLoockup_donvi() != null) {
            try {
                if (entity.getLoockup_donvi().getDonvitrungtam() == true) {
                    label.setValue("Đơn vị chính");
                }
            } catch (NullPointerException ex) {

            }
        }
        return label;
    }
}