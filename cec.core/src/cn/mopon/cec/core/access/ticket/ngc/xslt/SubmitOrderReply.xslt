<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/reply">
		<reply>
			<code><xsl:value-of select="code" /></code>
			<msg><xsl:value-of select="msg" /></msg>
			<ticketOrder>
				<voucher>
					<printCode><xsl:value-of select="printCode" /></printCode>
					<verifyCode><xsl:value-of select="verifyCode" /></verifyCode>
					<code><xsl:value-of select="voucherCode" /></code>
				</voucher>
			</ticketOrder>
		</reply>
	</xsl:template>
</xsl:stylesheet>