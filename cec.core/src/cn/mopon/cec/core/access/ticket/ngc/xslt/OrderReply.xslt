<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/reply">
		<data>
			<code><xsl:value-of select="code" /></code>
			<msg><xsl:value-of select="msg" /></msg>
			<ticketOrder>
				<cinemaOrderCode><xsl:value-of select="order/code" /></cinemaOrderCode>
				<channelOrderCode><xsl:value-of select="order/channelOrderCode" /></channelOrderCode>
				<voucher>
					<printCode><xsl:value-of select="order/printCode" /></printCode>
					<verifyCode><xsl:value-of select="order/verifyCode" /></verifyCode>
					<code><xsl:value-of select="order/voucherCode" /></code>
				</voucher>
				<ticketCount><xsl:value-of select="order/ticketCount" /></ticketCount>
				<status><xsl:value-of select="order/status" /></status>
				<amount><xsl:value-of select="order/amount" /></amount>
			</ticketOrder>
		</data>
	</xsl:template>
</xsl:stylesheet>