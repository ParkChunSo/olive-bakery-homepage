package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private MemberRepository memberRepository;

  public UserDetailsServiceImpl(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findById(username)
                  .orElseThrow(() -> new UsernameNotFoundException(username));
    return new User(member.getId(), member.getPw(), makeGrantedAuthority(member.getRole()));
  }

  private Set<GrantedAuthority> makeGrantedAuthority(Set<MemberRole> roles) {
    return roles.stream()
        .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
        .collect(Collectors.toSet());
  }
}
