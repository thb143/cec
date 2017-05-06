<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/OnlineTicketingServiceReply/DQueryTakeTicketInfoReply">
		<DQueryTakeTicketInfoReply>
			<xsl:attribute name="ErrorCode"><xsl:value-of select="@ErrorCode"/></xsl:attribute>
			<xsl:attribute name="ErrorMessage"><xsl:value-of select="@ErrorMessage"/></xsl:attribute>
			<xsl:attribute name="Id"><xsl:value-of select="@Id"/></xsl:attribute>
			<xsl:attribute name="Status"><xsl:value-of select="@Status"/></xsl:attribute>
			<Order>
				<voucher>
					<printCode><xsl:value-of select="PrintNo" /></printCode>
					<verifyCode><xsl:value-of select="VerifyCode" /></verifyCode>
					<status><xsl:value-of select="PrintFlag" /></status>
				</voucher>
				<orderItems>
					<xsl:for-each select="TakeTicketInfo">
						<orderItem>
							<barCode><xsl:value-of select="TwoCode" /></barCode>
							<seatCode><xsl:value-of select="SeatCode" /></seatCode>
						</orderItem>
					</xsl:for-each>
				</orderItems>
				<orderSnacks></orderSnacks>
			</Order>
		</DQueryTakeTicketInfoReply>
	</xsl:template>
</xsl:stylesheet>