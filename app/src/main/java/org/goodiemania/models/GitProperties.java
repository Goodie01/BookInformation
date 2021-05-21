package org.goodiemania.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GitProperties {
    private String tags; //git.tags
    private String buildVersion; //git.build.version
    private String commitUserName; //git.commit.user.name
    private String commitIdAbbrev; //git.commit.id.abbrev
    private String branch; //git.branch
    private String buildHost; //git.build.host
    private String buildTime; //git.build.time
    private String buildUsername; //git.build.user.name
    private String commitDescShort; //git.commit.id.describe-short
    private String commitDesc; //git.commit.id.describe
    private String buildUserEmail; //git.build.user.email
    private String commitId; //git.commit.id
    private String commitMessageShort; //git.commit.message.short
    private String closestTagName; //git.closest.tag.name
    private String closestTagCommit; //git.closest.tag.commit.count
    private String commitUserEmail; //git.commit.user.email
    private String commitTime; //git.commit.time
    private String dirty; //git.dirty
    private String commitMessageFull; //git.commit.message.full
    private String remoteOriginUrl; //git.remote.origin.url

    /**
     * Does magic.
     *
     * @return A formed properties file, generated from the git properties file
     */
    public static GitProperties fromPropsFile() {
        Properties props = new Properties();
        try {
            InputStream resource = GitProperties.class.getClassLoader().getResourceAsStream("git.properties");
            props.load(resource);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        GitProperties git = new GitProperties();

        git.tags = props.getProperty("git.tags");
        git.buildVersion = props.getProperty("git.build.version");
        git.commitUserName = props.getProperty("git.commit.user.name");
        git.commitIdAbbrev = props.getProperty("git.commit.id.abbrev");
        git.branch = props.getProperty("git.branch");
        git.buildHost = props.getProperty("git.build.host");
        git.buildTime = props.getProperty("git.build.time");
        git.buildUsername = props.getProperty("git.build.user.name");
        git.commitDescShort = props.getProperty("git.commit.id.describe-short");
        git.commitDesc = props.getProperty("git.commit.id.describe");
        git.buildUserEmail = props.getProperty("git.build.user.email");
        git.commitId = props.getProperty("git.commit.id");
        git.commitMessageShort = props.getProperty("git.commit.message.short");
        git.closestTagName = props.getProperty("git.closest.tag.name");
        git.closestTagCommit = props.getProperty("git.closest.tag.commit.count");
        git.commitUserEmail = props.getProperty("git.commit.user.email");
        git.commitTime = props.getProperty("git.commit.time");
        git.dirty = props.getProperty("git.dirty");
        git.commitMessageFull = props.getProperty("git.commit.message.full");
        git.remoteOriginUrl = props.getProperty("git.remote.origin.url");

        return git;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public String getCommitUserName() {
        return commitUserName;
    }

    public void setCommitUserName(String commitUserName) {
        this.commitUserName = commitUserName;
    }

    public String getCommitIdAbbrev() {
        return commitIdAbbrev;
    }

    public void setCommitIdAbbrev(String commitIdAbbrev) {
        this.commitIdAbbrev = commitIdAbbrev;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBuildHost() {
        return buildHost;
    }

    public void setBuildHost(String buildHost) {
        this.buildHost = buildHost;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getBuildUsername() {
        return buildUsername;
    }

    public void setBuildUsername(String buildUsername) {
        this.buildUsername = buildUsername;
    }

    public String getCommitDescShort() {
        return commitDescShort;
    }

    public void setCommitDescShort(String commitDescShort) {
        this.commitDescShort = commitDescShort;
    }

    public String getCommitDesc() {
        return commitDesc;
    }

    public void setCommitDesc(String commitDesc) {
        this.commitDesc = commitDesc;
    }

    public String getBuildUserEmail() {
        return buildUserEmail;
    }

    public void setBuildUserEmail(String buildUserEmail) {
        this.buildUserEmail = buildUserEmail;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getCommitMessageShort() {
        return commitMessageShort;
    }

    public void setCommitMessageShort(String commitMessageShort) {
        this.commitMessageShort = commitMessageShort;
    }

    public String getClosestTagName() {
        return closestTagName;
    }

    public void setClosestTagName(String closestTagName) {
        this.closestTagName = closestTagName;
    }

    public String getClosestTagCommit() {
        return closestTagCommit;
    }

    public void setClosestTagCommit(String closestTagCommit) {
        this.closestTagCommit = closestTagCommit;
    }

    public String getCommitUserEmail() {
        return commitUserEmail;
    }

    public void setCommitUserEmail(String commitUserEmail) {
        this.commitUserEmail = commitUserEmail;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public String getDirty() {
        return dirty;
    }

    public void setDirty(String dirty) {
        this.dirty = dirty;
    }

    public String getCommitMessageFull() {
        return commitMessageFull;
    }

    public void setCommitMessageFull(String commitMessageFull) {
        this.commitMessageFull = commitMessageFull;
    }

    public String getRemoteOriginUrl() {
        return remoteOriginUrl;
    }

    public void setRemoteOriginUrl(String remoteOriginUrl) {
        this.remoteOriginUrl = remoteOriginUrl;
    }
}
