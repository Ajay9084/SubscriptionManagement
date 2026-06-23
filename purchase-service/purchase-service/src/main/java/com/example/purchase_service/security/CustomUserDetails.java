//package com.example.purchase_service.security;
//
//
//import com.example.subscription.entity.User;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//@AllArgsConstructor
//public class CustomUserDetails implements UserDetails {
//
//	private final User user;
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities(){    //Spring Security calls this method after a user is authenticated
//		return user.getRoles()
//				.stream()
//				.map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
//				.collect(Collectors.toSet());
//	}
//
//	@Override
//	public String getPassword() {
//		return user.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return user.getUsername();
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}
//}