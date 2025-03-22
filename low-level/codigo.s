	.def	@feat.00;
	.scl	3;
	.type	0;
	.endef
	.globl	@feat.00
.set @feat.00, 0
	.file	"codigo.ll"
	.def	main;
	.scl	2;
	.type	32;
	.endef
	.globl	__real@40091eb851eb851f         # -- Begin function main
	.section	.rdata,"dr",discard,__real@40091eb851eb851f
	.p2align	3, 0x0
__real@40091eb851eb851f:
	.quad	0x40091eb851eb851f              # double 3.1400000000000001
	.text
	.globl	main
	.p2align	4
main:                                   # @main
.seh_proc main
# %bb.0:                                # %entry
	subq	$72, %rsp
	.seh_stackalloc 72
	.seh_endprologue
	movl	$15, 68(%rsp)
	movsd	__real@40091eb851eb851f(%rip), %xmm0 # xmm0 = [3.1400000000000001E+0,0.0E+0]
	movsd	%xmm0, 56(%rsp)
	movb	$1, 55(%rsp)
	leaq	.Lstr_texto(%rip), %rax
	movq	%rax, 40(%rsp)
	movq	40(%rsp), %rdx
	leaq	.L.str_s(%rip), %rcx
	callq	printf
	movb	55(%rsp), %al
	andb	$1, %al
	movzbl	%al, %edx
	leaq	.L.str_d(%rip), %rcx
	callq	printf
	movl	68(%rsp), %edx
	leaq	.L.str_d(%rip), %rcx
	callq	printf
	movsd	56(%rsp), %xmm0                 # xmm0 = mem[0],zero
	leaq	.L.str_f(%rip), %rcx
	movaps	%xmm0, %xmm1
	movq	%xmm0, %rdx
	callq	printf
	xorl	%eax, %eax
	addq	$72, %rsp
	retq
	.seh_endproc
                                        # -- End function
	.section	.rdata,"dr"
.L.str_s:                               # @.str_s
	.asciz	"%s"

.L.str_d:                               # @.str_d
	.asciz	"%d"

.L.str_f:                               # @.str_f
	.asciz	"%lf"

.Lstr_texto:                            # @str_texto
	.asciz	"\"zard\""

	.addrsig
	.addrsig_sym printf
	.globl	_fltused
