package com.lord.small_box.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;



@Entity
@Table(name="users")
public class AppUser implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="lastname")
	private String lastname;
	
	@Column(name="username", unique = true)
	private String username;
	
	@Column(name="email",unique = false)
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="account_non_locked")
	private boolean  accountNonLocked;
	
	@Column(name="account_non_expired")
	private boolean accountNonExpired; 
	
	@Column(name="credentials_non_expired")
	private boolean credentialsNonExpired;
	
	@Column(name="enabled")
	private boolean enabled;
	
	@ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
	@JoinTable(name="user_role_junction", joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
	inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName = "id")})
	private Set<Authority> authorities;
	
	@ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinTable(name = "user_organization_junction", joinColumns = { @JoinColumn(name="user_id", referencedColumnName = "id") },
	inverseJoinColumns = { @JoinColumn(name="organization_id", referencedColumnName = "id") })
	private List<Organization> organizations;
	
	@Column(name="main_organization_id")
	private long mainOrganizationId;
	
	/*@ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinTable(name = "user_organization_receiver_junction", joinColumns = { @JoinColumn(name="user_id", referencedColumnName = "id") },
	inverseJoinColumns = { @JoinColumn(name="organization_id", referencedColumnName = "id") })
	private List<Organization> organizationReceivers;*/
	
	
	
	
	public AppUser() {
		super();
	}
	
	public AppUser(List<Organization> organizations) {
		super();
		this.organizations = organizations;
		
	}
	
	public AppUser(String name,String lastname,String username,String email,String password,
	boolean accountNonLocked, boolean accountNonExpired,boolean credentialsNonExpired,boolean enabled,Set<Authority> authorities) {
		super();
		this.name =name;
		this.lastname = lastname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.accountNonLocked = accountNonLocked;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.authorities = authorities;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public void setAuhtorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public List<Organization> getOrganizations(){
		return organizations;
	}
	
	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}
	
	/*public List<Organization> getOrganizationReceivers(){
		return organizationReceivers;
	}
	public void setOrganizationReceivers(List<Organization> organizationReceivers) {
		this.organizationReceivers = organizationReceivers;
	}*/
	
	public Long getMainOrganizationId() {
		return mainOrganizationId;
	}
	
	public void setMainOrganizationId(Long mainOrganizationId) {
		this.mainOrganizationId = mainOrganizationId;
	}
	
	
	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
